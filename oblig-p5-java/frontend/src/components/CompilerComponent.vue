<template>
    <div>
      <h1 class="title">Python Code Compiler</h1>
      <h2>P5 in IDATT2104</h2>
      <label class="label">Enter code here</label>
      <div class="code-container">
        <div class="line-numbers">
          <span v-for="i in lineCount" :key="i">{{ i }}</span>
        </div>
        <textarea class="code-area" v-model="code" @keydown.tab.prevent="insertTab"></textarea>
      </div>
      <div class="button" @click="compileCode">
        <p>Compile</p>
      </div>
    </div>
    <h1 class="title">Compiled code</h1>
    <div class="compiled-container">
      <pre class="compiled-code">{{ output }}</pre>
    </div>
  </template>

<script>
import axios from "axios";

export default {
    name: "CompilerComponent",
    props: {
        msg: String,
    },
    data() {
        return {
        code: 'print("Hello world!")',
        output: "",
        };
    },
    created() {
        // update the line count when the component is created
        this.updateLineCount();
    },
    mounted() {
        // update the line count when the component is mounted
        this.updateLineCount();
    },
    methods: {
        async compileCode() {
            const response = await axios.post("http://localhost:8080/compile", {
                code: this.code,
            });
            console.log(response.data);
            this.output = response.data;
        },
        insertTab(event) {
        const textarea = event.target;
        const start = textarea.selectionStart;
        const end = textarea.selectionEnd;

        const newLineRegex = /\r|\n/; // regex to match new line character
        const previousLine = this.code.substring(0, start).split(newLineRegex).pop(); // get the previous line
        const indentation = previousLine.match(/^\s*/)[0]; // get the existing indentation of the previous line
        const indentationLength = indentation.length; // get the length of the existing indentation
        const tab = "    "; // four spaces
        const spaces = tab.substring(indentationLength); // calculate how many spaces to add based on existing indentation

        // Insert four spaces at the current cursor position
        textarea.value = textarea.value.substring(0, start) + spaces + textarea.value.substring(end);

        // Move the cursor to the right of the inserted spaces
        textarea.selectionStart = textarea.selectionEnd = start + spaces.length;

        // Prevent the default behavior of the "Tab" key
        event.preventDefault();
        },
        updateLineCount() {
        // calculate the number of lines in the code
        const lineCount = this.code.split(/\r\n|\r|\n/).length;
        this.lineCount = lineCount;
        },
    },
    watch: {
        code() {
        this.updateLineCount();
        },
    },
}


</script>

<style scoped>
.code-container {
  position: relative;
  min-width: 500px;
}

.line-numbers {
  position: absolute;
  left: 25%;
  top: 0;
  width: 5%;
  height: 100%;
  padding: 0.5em 0;
  font-family: monospace;
  font-size: 12px;
  color: #aaa;
  pointer-events: none;
  z-index: 1;
  text-align: right;
}

.label {
  font-weight: bold;
}

.code-area {
  margin-top: 3.5px;  
  position: relative;
  z-index: 0;
  width: 40%;
  height: 200px;
  font-family: monospace;
  font-size: 12px;
  resize: none;
  border: none;
  outline: 1px solid #a8a9a9;
  border-radius: 5px;
  padding-left: calc(5% + 10px);
}

.line-numbers span {
  display: block;
}


.code-area:focus {
  outline: none;
}

.compiled-container {
    margin-top: 50px;
    margin: 0 auto;
    width: 50%;
}

.title {
  background: rgb(137, 136, 136);
  border-radius: 10px;
  width: 50%;
  max-width: 450px;
  min-width: 400px;
  margin: 0 auto;
}

.compiled-code {
  text-align: left;
  background: rgb(153, 152, 152);
  border-radius: 10px;
  padding-left: 3%;
  padding-top: 3%;
  padding-bottom: 3%;
}

.button {
    background: rgb(137, 136, 136);
    max-width: 150px;
    margin: 0 auto;
    color: white;
    font-size: 20px;
    border-radius: 5px 5px 5px 5px;
    font-family:'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
    margin-bottom: 5%;
}
</style>