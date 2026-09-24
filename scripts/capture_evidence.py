"""Capture saved experiment/test logs and generated plots using headless Chrome.

Run from any directory: python scripts/capture_evidence.py [--chrome PATH]
Only Python's standard library is needed. These are browser views of actual logs,
not simulated terminal sessions. Run Maven tests/experiments before capturing.
"""

import argparse
import html
import os
from pathlib import Path
import shutil
import subprocess


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--chrome', help='Path to the Chrome executable')
    args = parser.parse_args()
    chrome = args.chrome or shutil.which('google-chrome') or shutil.which('chromium')
    if not chrome:
        for root in (os.environ.get('PROGRAMFILES'), os.environ.get('LOCALAPPDATA')):
            if root:
                candidate = Path(root) / 'Google/Chrome/Application/chrome.exe'
                if candidate.is_file():
                    chrome = str(candidate)
                    break
    if not chrome:
        parser.error('Chrome not found; supply --chrome PATH')

    root = Path(__file__).resolve().parents[1]
    output = root / 'docs/screenshots'
    temporary = root / 'target/evidence'
    output.mkdir(parents=True, exist_ok=True)
    temporary.mkdir(parents=True, exist_ok=True)
    program = (root / 'results/program-output.txt').read_text(encoding='utf-8')
    program = program[program.index('=== Divide-and-Conquer'):]
    tests = (root / 'results/test-output.txt').read_text(encoding='utf-8')
    tests = tests[tests.index('[INFO] Running com.example.AlgorithmTest'):]
    tests = '\n'.join(line for line in tests.splitlines() if line.strip())
    pages = [
        ('program_output', 'Program output', '<pre>' + html.escape(program) + '</pre>', 760),
        ('test_results', 'JUnit test results', '<pre>' + html.escape(tests) + '</pre>', 650),
        ('plots', 'Measured time and recursion depth', ''.join(
            '<img src="' + (root / 'docs/plots' / filename).as_uri() + '">'
            for filename in ('time_vs_n.png', 'depth_vs_n.png')), 1610),
    ]
    for name, title, body, height in pages:
        page = temporary / (name + '.html')
        page.write_text('''<!doctype html><meta charset="utf-8">
<style>
body { margin: 36px; color: #18243a; background: #f4f6fa; font-family: Arial, sans-serif; }
h1 { font-size: 28px; margin-bottom: 10px; }
p { color: #516079; font-size: 16px; margin-bottom: 25px; }
pre { background: #142033; color: #eaf2ff; padding: 25px; border-radius: 12px;
      font: 17px/1.65 Consolas, monospace; white-space: pre-wrap; overflow-wrap: anywhere; }
img { display: block; width: 1100px; margin-bottom: 20px; }
</style><h1>''' + html.escape(title) + '</h1><p>Assignment 1 / '
                        + ('Generated charts from results/results.csv' if name == 'plots'
                           else 'Browser-rendered saved console output; source: results/'
                           + ('program-output.txt' if name == 'program_output' else 'test-output.txt'))
                        + '</p>' + body, encoding='utf-8')
        command = [chrome, '--headless', '--disable-gpu', '--no-first-run',
                   '--no-default-browser-check', '--hide-scrollbars',
                   '--allow-file-access-from-files',
                   '--user-data-dir=' + str(temporary / 'chrome-profile'),
                   '--screenshot=' + str(output / (name + '.png')),
                   '--window-size=1180,' + str(height), page.as_uri()]
        result = subprocess.run(command, capture_output=True, timeout=45)
        if result.returncode or not (output / (name + '.png')).is_file():
            raise RuntimeError(result.stderr.decode(errors='replace'))
        print('Saved docs/screenshots/' + name + '.png')


if __name__ == '__main__':
    main()
