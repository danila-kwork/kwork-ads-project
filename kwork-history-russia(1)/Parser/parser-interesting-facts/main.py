import os

import requests
import json
from bs4 import BeautifulSoup

url = "https://fishki.net/1748059-100-interesnyh-faktov-o-rossii.html"
filePath = "interesting_facts.json"

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

interesting_facts = []

for item in soup.find_all("div", class_="container_gallery_description"):
    text = item.text.strip().replace("\n", "").replace("\t", '')
    if text != "":
        interesting_facts.append({"text": text})

if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(interesting_facts, fp)
