#include <iostream>
#include <utility>
#include <vector>
#include <thread>
#include "functional"
#include "list"
#include "mutex"
#include "condition_variable"
#include <queue>

using namespace std;

class Workers
{
public:
    explicit Workers(int num_threads);
    void start();
    void post(const std::function<void()>& task);
    void post_timeout(const std::function<void()>& task, int millis);
    void stop();
    void join();

private:
    mutex tasks_mutex;
    condition_variable cv;
    vector<thread> worker_threads;
    queue<function<void()>> tasks; //queue over list pga fifo, concurrency (thread safe), effektiv, simpel

    bool running = true;

    void worker_func();
};

Workers::Workers(int num_threads) {
    worker_threads.resize(num_threads);
}

void Workers::start() {
    // Create and start each worker thread
    for (auto& worker : worker_threads) {
        worker = std::thread(&Workers::worker_func, this);
    }
}

void Workers::post(const function<void()>& task) {
    unique_lock<mutex> lock(tasks_mutex);
    tasks.push(task);
    cv.notify_one();
}

void Workers::post_timeout(const function<void()>& task, int millis) {

    //Løsning der det kjøres sekvensielt
    /*
    this_thread::sleep_for(std::chrono::milliseconds(millis));
    post(task);
     */

    //Løsning der det kjøres etter hvilken som bruker kortest tid

    thread([this, task, millis] {
        // Create a new thread that waits for the specified time period and then adds the task to the queue
        this_thread::sleep_for(std::chrono::milliseconds(millis));
        post(task);
    }).detach();

}


void Workers::stop() {
    {
        unique_lock<mutex> lock(tasks_mutex);
        running = false;
    }
    cv.notify_all();
}

void Workers::join() {
    for (auto& worker : worker_threads) {
        if (worker.joinable()) {
            worker.join();
        }
    }
}

void Workers::worker_func() {
    while (true) {
        function<void()> task;
        {
            unique_lock<mutex> lock(tasks_mutex);
            // Wait on the condition variable, only continue when running is true or tasks is not empty
            cv.wait(lock, [this]
            {
                return !running || !tasks.empty();
            });
            // If running is false and tasks is not empty, break the loop
            if (!running && !tasks.empty()) {
                break;
            }
            task = std::move(tasks.front());
            tasks.pop();
        }

        if (task)
            task(); // Run task outside of mutex lock
        else
            stop();
    }
}

void demo() {
    Workers workers_threads(4);
    Workers event_loop(1);
    workers_threads.start();
    event_loop.start();
    workers_threads.post([] { cout << "A" << endl; });
    workers_threads.post([] { cout << "B" << endl; });
    event_loop.post([] { cout << "C" << endl; });
    event_loop.post([] { cout << "D" << endl; });
    workers_threads.join();
    event_loop.join();
    workers_threads.stop();
    event_loop.stop();

}

void demo_timer() {
    Workers event_loop(1);
    event_loop.start();
    event_loop.post_timeout([] { cout << "1 sekund" << endl; }, 1000);
    event_loop.post([] { cout << "Instant" << endl; });
    event_loop.post_timeout([] { cout << "5 sekunder" << endl; },5000);
    event_loop.post_timeout([] { cout << "2 sekunder" << endl; },2000);
    event_loop.join();
    event_loop.stop();
}



int main() {
    //demo();
    demo_timer();
    return 0;
}
