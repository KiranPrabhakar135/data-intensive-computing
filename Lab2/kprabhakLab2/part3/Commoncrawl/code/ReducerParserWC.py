import requests
import re
from nltk.corpus import stopwords
from bs4 import BeautifulSoup
import string
from nltk.stem import PorterStemmer
from nltk.tokenize import sent_tokenize, word_tokenize
ps = PorterStemmer()
import  pandas as pd

def construct_dictionary(path):
    pairs = []
    for i in range(0,3):
        with open(path+ "//part-r-0000" + str(i), "r", encoding="utf-8") as f:
            pairs.extend(f.readlines())
    return pairs


def wc_counts(path, no_check = False):
    raw_pairs = construct_dictionary(path)
    print(len(raw_pairs))
    sub_topics = ['google', 'amazon', 'microsoft', 'facebook', 'apple']
    pairs = []
    top_stem = []
    for pair in raw_pairs:
        details = pair.split()
        stem_word = ps.stem(details[0])
        if stem_word not in top_stem or details[0] in sub_topics:
            top_stem.append(stem_word)
            pairs.append((details[0], details[1]))
    list.sort(pairs, key=lambda x: int(x[1]), reverse=True)
    if no_check:
        return pairs

    print(pairs[:10])
    top_ten = pairs[:10]
    missing_sub_topics = []
    top_ten_words = [t[0] for t in top_ten]
    for topic in sub_topics:
        if topic not in top_ten_words:
            missing_sub_topics.append(topic)
    append_to_ten = []
    if not append_to_ten:
        return top_ten
    for pair in pairs:
        if pair[0] in missing_sub_topics:
            append_to_ten.append(pair)

    top_ten[-len(append_to_ten):] = append_to_ten
    return top_ten

# Provide the path of the reducer output of word count
wc_reducer_output_path = "reducer_output_cc_wc"
top_ten = wc_counts(wc_reducer_output_path)
print(top_ten)