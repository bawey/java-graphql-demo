#!/bin/python3

import requests
import os
import json

def ask(message='Press any key to continue...'):
    return input(message)

def client():
    files = [item for item in os.scandir() if item.is_file() and item.name.endswith('.graphql')]
    print(f'Found {len(files)} files that look like they contain some GraphQL queries')
    for file in files:
        with open(file, 'r') as input_file:
            query_string = input_file.read()
            print(f'Current file: {file.path}, the query will be: \n{query_string}')
            ask()
            response = requests.post('http://localhost:8080/graphql', json={'query': query_string})
            if response.status_code == 200:
                print(json.dumps(response.json(), sort_keys=True, indent=4))
            else:
                print(f'Got a status code of {response.status_code}')
                print(response.content)
            ask()


if __name__ == '__main__':
    client()