import re

with open("build/reports/tests/test/index.html", "r") as f:
    content = f.read()
    failed_tests = re.findall(r'<a href="classes/.*?">(.*?)</a>', content)
    print("Failed Tests:", failed_tests)
