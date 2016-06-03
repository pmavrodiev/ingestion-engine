#!/bin/bash

rm -rf flume-channel
flume-ng agent --name flumeagent --conf-file flume_CRASH.conf --plugins-path flume-plugins/

