import os

import requests
import json
from bs4 import BeautifulSoup

filePath = 'news.json'
url = 'https://www.upi.com/Sports_News/'
page = 1
page_size = input()

news = []

while page <= int(page_size):
    print(page)
    response = requests.get(f'{url}p{page}/')
    soup = BeautifulSoup(response.text, 'html.parser')
    items = soup.find('div', class_='row story list').find_all_next('div', class_='col-md-12')

    for item in items:
        image_container = item.findNext('div', class_='col-md-4 col-sm-4 col-xs-4')

        if image_container:
            web_url = item.findNext('a', class_='row')['href']
            text_container = item.findNext('div', class_='col-md-8 col-sm-8 col-xs-8')
            title = text_container.find('div', class_='title').text
            description = text_container.find('div', class_='content').text
            image_url = image_container.find('img')['src']
            print(title)

            if image_url == '/img/clear.gif':
                image_url = None

            news_item = {
                'title': title,
                'description': description,
                'image_url': image_url,
                'web_url': web_url
            }

            if news_item not in news:
                news.append(news_item)

    page += 1

if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(news, fp)
