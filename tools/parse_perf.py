#!/usr/bin/env python3
import csv
import sys
from statistics import mean

if len(sys.argv) < 2:
    print("Usage: parse_perf.py <csv-file>")
    sys.exit(2)

file = sys.argv[1]
rows = []
with open(file, newline='') as f:
    reader = csv.DictReader(f)
    for r in reader:
        rows.append(r)

scenario_durations = []
load_event_ends = []

for r in rows:
    if 'durationMs' in r and r['durationMs'].strip():
        try:
            scenario_durations.append(float(r['durationMs']))
        except:
            pass
    if 'loadEventEnd' in r and r['loadEventEnd'].strip():
        try:
            load_event_ends.append(float(r['loadEventEnd']))
        except:
            pass

print(f"Rows parsed: {len(rows)}")
if scenario_durations:
    print(f"Average scenario duration ms: {mean(scenario_durations):.2f}")
else:
    print("No scenario duration data found")

if load_event_ends:
    print(f"Average loadEventEnd ms: {mean(load_event_ends):.2f}")
else:
    print("No loadEventEnd data found")

