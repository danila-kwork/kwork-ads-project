import os

import requests
import json
from bs4 import BeautifulSoup

filePath = 'questions.json'

urls = []

base_url = "https://kupidonia.ru/list/история%20России"
response = requests.get(base_url)
soup = BeautifulSoup(response.text, 'html.parser')

for item in soup.find_all("a", class_="item"):
    urls.append({
        "title": item.find_next("img")["alt"],
        "image": f"https://kupidonia.ru{item.find_next('img')['src']}",
        "question": f"https://kupidonia.ru/{item['href']}",
        "answer": f"https://kupidonia.ru/{item['href'].replace('viktoriny', 'viktoriny-answers')}"
    })

questions = []

for url in urls:
    response = requests.get(url["question"])
    soup = BeautifulSoup(response.text, 'html.parser')

    block_question = soup.findAll("div", class_="block_question")
    print(url["title"])
    question_item_url = {
        "title": url["title"],
        "image": url["image"],
        "content": []
    }

    for block in block_question:
        question = {
            "question": "",
            "answer": "",
            "answers": []
        }

        question_text = block.find_next("h3", class_="question").find_next("div").text

        question["question"] = question_text

        answers_block = block.find_next("div", class_="answer")\
            .find_all("div", class_="answer-block")

        for answer_block in answers_block:
            answer = answer_block.find_next().find_next()
            print(answer)
            question["answers"].append({"answer": answer.text})

        question_item_url["content"].append(question)

    response = requests.get(url["answer"])
    soup = BeautifulSoup(response.text, 'html.parser')

    for index, item in enumerate(soup.find_all("div", class_="block_question")):
        question_text = item.find_next("h3", class_="question").find_next("div").text
        answer_text = item.find_next("p", class_="answer-text").text

        question = question_item_url["content"][index]

        if question["question"] == question_text:
            question["answer"] = answer_text

    questions.append(question_item_url)

if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(questions, fp)
