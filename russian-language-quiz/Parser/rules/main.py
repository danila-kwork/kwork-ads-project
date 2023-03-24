import os

import requests
import json
from bs4 import BeautifulSoup

filePath = 'rules.json'
base_url = 'https://russkiypro.com/rules/'
page = 0
school_class = 1

rules_urls = []
rules = []

while school_class <= 11:
    response = requests.get(f'{base_url}{school_class}-class')
    soup = BeautifulSoup(response.text, 'html.parser')

    list_rules = soup.find('div', class_='list expos').find_all_next('h3')

    for rule in list_rules:
        url = rule.next['href']
        title = rule.next.text

        print(title)

        rules_urls.append({'class': school_class, 'url': url, 'title': title})

    school_class += 1

for rule_url in rules_urls:
    response = requests.get(rule_url['url'])
    soup = BeautifulSoup(response.text, 'html.parser')

    content = soup.find('div', class_='description')

    print(f'{rule_url["class"]}//{rule_url["title"]}')

    rules.append({
        'class': rule_url['class'],
        'url': rule_url['url'],
        'title': rule_url['title'],
        'content': str(content.text)
    })

if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(rules, fp)
