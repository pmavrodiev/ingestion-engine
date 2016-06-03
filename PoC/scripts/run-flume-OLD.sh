#!/bin/bash

rm -rf flume-channel
flume-ng agent --name flumeagent --conf-file flume_OLD.conf --plugins-path flume-plugins/
