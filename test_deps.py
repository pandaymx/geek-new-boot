import urllib.request
import xml.etree.ElementTree as ET

url = "https://repo1.maven.org/maven2/software/amazon/awssdk/aws-sdk-java/maven-metadata.xml"
try:
    response = urllib.request.urlopen(url)
    xml_data = response.read()
    root = ET.fromstring(xml_data)
    versions = [v.text for v in root.findall(".//version")]
    print("Latest versions:", versions[-10:])
except Exception as e:
    print(f"Error fetching versions: {e}")
