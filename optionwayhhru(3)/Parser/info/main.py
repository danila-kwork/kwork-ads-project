import os

import requests
import json
from bs4 import BeautifulSoup

filePath = 'info.json'
url = 'https://www.ranker.com/list/list-of-famous-football-players/reference?page='
page = 1
page_size = input('enter page size')
urls = []
info_football = []
info_football_wikipedia = []

while page <= int(page_size):
    print(page)
    response = requests.get(f'{url}{page}')
    soup = BeautifulSoup(response.text, 'html.parser')
    items = soup \
        .find('ul', class_='gridView_list__fjKMd undefined') \
        .find_all_next('li')

    for item in items:
        name_container = item.find_next('div', class_='NodeName_nameWrapper__n32Nb')

        if name_container:
            name = name_container.find_next('a', class_='listItem_name__Qq_Y8 listItem_nameLink__eLagT').text
            url_info = name_container.find_next('a', class_='listItem_name__Qq_Y8 listItem_nameLink__eLagT')['href']

            url_item = {
                'name': name,
                'url': f'https://www.ranker.com{url_info}'
            }

            if url_item not in urls:
                urls.append(url_item)

    page += 1

for url in urls:
    print(url['url'])
    response = requests.get(url['url'])
    soup = BeautifulSoup(response.text, 'html.parser')

    try:
        icon = soup.find('img', 'NodeThumbnail_main__aCWHm')['src']
        desc = soup.find('div',
                         class_='richText_container__p5dag richText_italic__TeWpZ Bio_wikiText__L4JbE undefined').next.text
        wikipedia = soup.find('div',
                              class_='richText_container__p5dag richText_italic__TeWpZ Bio_wikiText__L4JbE undefined').find_next(
            'a', class_='richText_readMore__spaOL')['href']

        info_football.append({
            'icon': icon,
            'name': url['name'],
            'url': url['url'],
            'desc': desc,
            'wikipedia': wikipedia,
        })
    except Exception:
        icon = soup.find('img', 'NodeThumbnail_main__aCWHm')['src']
        desc = soup.find('div',
                         class_='richText_container__p5dag richText_italic__TeWpZ Bio_wikiText__L4JbE undefined').next.text

        info_football.append({
            'icon': icon,
            'name': url['name'],
            'url': url['url'],
            'desc': desc,
        })

for index, info in enumerate(info_football):
    try:
        wikipedia_url = info['wikipedia']
        print(wikipedia_url)

        response = requests.get(wikipedia_url)
        soup = BeautifulSoup(response.text, 'html.parser')

        desc_full_page = soup.find('div', class_='mw-parser-output')
        desc_full = desc_full_page.find_all_next('p')
        desc_full_text = []

        for desc in desc_full:
            desc_full_text.append(desc.text)

        info_football_wikipedia.append({
            'id': index,
            'icon': info['icon'],
            'name': info['name'],
            'url': info['url'],
            'desc': info['desc'],
            'full_desc': '\n'.join(desc_full_text),
            'wikipedia_url': wikipedia_url
        })
    except Exception:
        info_football_wikipedia.append({
            'id': index,
            'icon': info['icon'],
            'name': info['name'],
            'url': info['url'],
            'desc': info['desc'],
        })


if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(info_football_wikipedia, fp)
