import os

import requests
import json
from bs4 import BeautifulSoup

url = 'https://profilaktica.ru/sotsialnye-seti/100-korotkikh-i-interesnykh-faktov-o-cheloveke/'
filePath = 'data.json'

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

data = []

items = soup.find('ol').find_all_next('li')

for item in items:
    text = item.next.text
    if text and text != ' ':
        print(text)
        data.append({'text': text})

url = 'https://karelinform.ru/news/2016-10-27/pochti-100-interesnyh-faktov-o-cheloveke-i-ego-organizme-2186329'

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

items = soup.find_all('div', class_='Common_common__MfItd')

for item in items:
    text = item.next.text
    if text and text != ' ':
        print(text)
        data.append({'text': text})

url = 'https://stethoscopes.ru/blogs/poleznoe/facty'

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

items = soup.find('ol').find_all_next('li')

for item in items:
    text = item.text
    if text and text != ' ':
        print(text)
        data.append({'text': text})

url = 'https://1gai.ru/publ/524633-40-udivitelnyh-faktov-iz-biologii-glaza-cheloveka-samyj-staryj-cvetok-v-mire-i-drugoe.html'

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

items = soup.find_all('h2')

for item in items:
    text = item.text
    if text and text != ' ':
        print(text)
        data.append({'text': text})

url = 'https://100-faktov.ru/30-interesnyx-fatov-po-biologii/'

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

items = soup.find_all('p')

for item in items:
    text = item.text
    if text and text != ' ':
        print(text)
        data.append({'text': text})

url = 'http://xn--80aexocohdp.xn--p1ai/20-%D0%B8%D0%BD%D1%82%D0%B5%D1%80%D0%B5%D1%81%D0%BD%D1%8B%D1%85-%D1%84%D0%B0%D0%BA%D1%82%D0%BE%D0%B2-%D0%BE-%D0%B1%D0%B8%D0%BE%D0%BB%D0%BE%D0%B3%D0%B8%D0%B8/'

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

items = soup.find('ol').find_all_next('li')

for item in items:
    text = item.text
    if text and text != ' ':

        if text == '20 интересных фактов о медузах':
            break

        print(text)
        data.append({'text': text})

if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(data, fp)
