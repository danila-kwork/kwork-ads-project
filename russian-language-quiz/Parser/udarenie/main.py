import os

import requests
import json
from bs4 import BeautifulSoup

filePath = 'rules.json'
base_url = 'https://russkiypro.com/udarenie?page='
page = 1
page_size = 24

words_urls = []
words = []

while page <= page_size:
    response = requests.get(f'{base_url}{page}')
    soup = BeautifulSoup(response.text, 'html.parser')

    items = soup.find('div', class_='list').find_all_next('h3')

    for item in items:
        url = item.next['href']
        title = item.next.text

        print(title)

        words_urls.append({'url': url, 'title': title})

    page += 1

for word_url in words_urls:
    response = requests.get(word_url['url'])
    soup = BeautifulSoup(response.text, 'html.parser')

    content = soup.find('div', class_='description')

    print(word_url["title"])

    words.append({
        'url': word_url['url'],
        'title': word_url['title'],
        'content': str(content.text)
    })


if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(words, fp)
