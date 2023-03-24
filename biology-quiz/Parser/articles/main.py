import os

import requests
import json
from bs4 import BeautifulSoup

filePath = 'articles.json'
base_url = 'https://nauchforum.ru/archive/article/181?page='
page = 0
page_size = input('page_size')

article_urls = []
articles = []

while page <= int(page_size):
    response = requests.get(f'{base_url}{page}')
    soup = BeautifulSoup(response.text, 'html.parser')

    archive_list = soup.find('ul', class_='archive-list').find_all_next('li')

    for item in archive_list:
        url = item.findNext('a')['href']
        title = item.findNext('a').text.replace('\n', '')
        div_all = item.find_all_next('div')
        author = div_all[1].text.replace('\n', '')
        teacher = div_all[2].text.replace('\n', '')
        article_urls.append(
            {
                'url': f'https://nauchforum.ru{url}',
                'title': title,
                'author': author,
                'teacher': teacher
            }
        )

    page += 1

for url in article_urls:
    if url['url'] and url['author'] and url['teacher'] and url['title']:
        response = requests.get(url['url'])
        soup = BeautifulSoup(response.text, 'html.parser')

        html = soup.find('div', class_='content clearfix node-content body-article')

        if html:
            print(url['title'])

            articles.append(
                {
                    'html': str(html.text),
                    'url': url['url'],
                    'title': url['title'],
                    'author': url['author'],
                    'teacher': url['teacher']
                }
            )

if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(articles, fp)
