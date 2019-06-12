# To run: python just_text.py > text
###
from glob import glob
#
from nltk.corpus import stopwords
import warc

# List any of the WARC files found in the data folder
warc_files = glob("C:\DIC\Labs\Lab2\CC-MAIN-20190121172846-20190121194846-00444.warc.gz")

# Process each of the WARC files we found
files_processed = 0
for fn in warc_files:
  f = warc.open(fn)
  for record in f:
    url = record.header.get('warc-target-uri', None)
    if not url:
      continue
    text = record.payload.read()
    #
    print(url)
    print(text)


def construct_dictionary():
  pairs = []
  for i in range(0, 3):  # + str(i)
    with open("reducer_output_cc" + "//part-r-0000" + str(i), "r", encoding="utf-8") as f:
      pairs.extend(f.readlines())
  return pairs

def test():
    eng_stopwords = set(stopwords.words('english'))
    custom_stopwords = []
    with open("stopwords.txt", "r", encoding="utf-8") as f:
      custom_stopwords = f.readlines()
    custom_stopwords = [word.replace("\n", "") for word in custom_stopwords if
                        word != "\n" and word not in eng_stopwords]
    eng_stopwords.update(custom_stopwords)
    # print(eng_stopwords)
    raw_pairs = construct_dictionary()
    pairs = []
    words = []
    for pair in raw_pairs:
      details = pair.split()
      if details[0] + " " + details[1] not in words:
        words.append(details[0] + " " + details[1])
        pairs.append((details[0] + " " + details[1], details[2]))
    print(pairs)
    list.sort(pairs, key=lambda x: int(x[1]), reverse=True)
    print(pairs[0:50])

# test()



import json
import string
import re

import os

path = 'C:\\Users\\raman\\Desktop\\Buffalo\\sem1\\new-python-crawler\\tweets\\Robotics'
files = []

for file in os.listdir(path):
   if file.endswith(".json"):
       files.append(os.path.join(path,file))
count = 0
for file in files:
   with open(file) as json_file:
       data = json.load(json_file)
       for d in data:
           d['text'] = " ".join([word for word in d['text'].split()
                                   if 'http' not in word and
                                 'pic' not in word and
                                 '@' not in word and
                                 '<' not in word])
           text = d['text'].translate(str.maketrans('', '', string.punctuation))
           text = re.sub('[^A-Za-z0-9 ]', '', text.lower())
           text = re.sub(' +', ' ', text)
           print(text + "\n")
           with open("Robotics.txt" ,"a+") as output:
               output.write(text + "\n")
           count += 1
print(count)