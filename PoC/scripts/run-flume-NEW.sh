#!/bin/bash

rm -rf flume-channel
flume-ng agent --name flumeagent --conf-file flume_NEU.conf --plugins-path flume-plugins/

