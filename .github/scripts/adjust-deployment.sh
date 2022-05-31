#!/usr/bin/env bash
mkdir ./public
mkdir ./public/target
mkdir ./public/target/scala-3.1.2

cp ./js/target/scala-3.1.2/js-fastopt.js ./public/target/scala-3.1.2/js-fastopt.js
cp ./js/index.html ./public/index.html
