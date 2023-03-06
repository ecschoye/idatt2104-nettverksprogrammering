#include "iostream"
#include "thread"
#include <vector>
#include <chrono>
#include "cmath"
#include <map>

using namespace std;

mutex num_mutex;

bool isPrime(int n) {
    if (n <= 1) return false;
    if (n <= 3) return true;
    if ((n & 1) == 0) return false;

    int x = int(sqrt(n));

    for (int i = 3; i <= x; i+=2) {
        if (n % i == 0) return false;
    }
    return true;
}

void worker(int j, int start, int end, vector<int> &primes, map<int,int>& m) {
    for (int i = start; i <end; ++i) {
        if (isPrime(i)){
            num_mutex.lock();
            primes.push_back(i);
            m[j] += 1;
            num_mutex.unlock();
        }
    }
}

int main() {
    int start;
    int end;
    int N;
    vector<thread> threads;
    vector<int> primes;
    map<int, int> map;

    cout << "Enter starting number: ";
    cin >> start;

    cout << "Enter ending number: ";
    cin >> end;

    cout << "Enter amount of threads: ";
    cin >> N;

    threads.reserve(N);

    int block = (end - start + 1) / N;

    auto timeStart = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < N; ++i) {
        threads.emplace_back(worker, i, start + (block * i), start + (block * (i + 1)), ref(primes), ref(map));
    }

    for(auto &thread : threads) {
        thread.join();
    }

    auto timeStop = chrono::high_resolution_clock::now();

    auto time = duration_cast<chrono::milliseconds>(timeStop - timeStart);

    sort(primes.begin(), primes.end());

    unsigned int count = primes.size();

    for (auto prime : primes) {
        cout << prime << endl;
    }

    for (auto i : map) {
        cout << "Thread: " << i.first << " | Amount: " << i.second << " primes" << endl;
    }

    cout << "Done" << endl;
    cout << "There are " << count << " primes." << endl;
    cout << "Time " << time.count() << "ms" << endl;

    return 0;
}