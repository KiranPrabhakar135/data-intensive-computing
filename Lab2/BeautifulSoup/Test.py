import requests
import re
from nltk.corpus import stopwords
from bs4 import BeautifulSoup
import string
from nltk.stem import PorterStemmer
from nltk.tokenize import sent_tokenize, word_tokenize
ps = PorterStemmer()
import  pandas as pd

def construct_dictionary():
    pairs = []
    for i in range(0,3): #+ str(i)
        with open("reducer_output_cc_wc"+ "//part-r-0000" +  str(i), "r", encoding="utf-8") as f:
            pairs.extend(f.readlines())
    return pairs


def wc_counts(no_check = False):
    raw_pairs = construct_dictionary()
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
    top_ten = pairs[:20]
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
    # for to_append in append_to_ten:
    #     top_ten.reverse()
    #     for i in range(len(top_ten)):
    #         if top_ten[i][0] not in sub_topics:
    #             top_ten[i] = (to_append[0],to_append[1])
    #             break

    return top_ten



def co_counts_tw():
    raw_pairs = construct_dictionary()
    print(len(raw_pairs))
    sub_topics = ['google', 'amazon', 'microsoft', 'facebook', 'apple']
    pairs = []
    top_stem = []
    for pair in raw_pairs:
        details = pair.split()
        # stem_word = ps.stem(details[1])
        # if (pair,stem_word) not in top_stem:
        #     top_stem.append((pair,stem_word))
        # if (details[1] + " " + details[0] not in pairs):
        pairs.append((details[0] + " " + details[1], details[2]))
    list.sort(pairs, key=lambda x: int(x[1]), reverse=True)
    print(pairs[:50])
    top_ten = pairs[:10]
    top_ten = [item for item in top_ten if
               item[0].split()[1] + " " + item[0].split()[0] not in [t[0] for t in top_ten]]
    top_ten = [item for item in top_ten if
               item[0].split()[0]not in item[0].split()[1]]
    offset = 10 - len(top_ten)
    top_ten.extend(pairs[11: 11 + offset])
    words_in_top = [word[0].split()[0] for word in top_ten]
    append_to_ten_words = [word for word in sub_topics if word not in words_in_top]
    append_to_ten = [pair for pair in pairs[:50] if pair[0].split()[0] in append_to_ten_words]
    # j = 0
    # for i in range(len(top_ten)):
    #     if top_ten[i][0] not in sub_topics and top_ten[i][1] not in sub_topics:
    #         top_ten[i] = append_to_ten[j]
    #         j += 1
    #         if j == len(append_to_ten):
    #             break

    # top_ten[-len(append_to_ten):] = append_to_ten
    list.sort(top_ten, key=lambda x: int(x[1]), reverse=True)
    return top_ten



def co_counts():
    raw_pairs = construct_dictionary()
    print(len(raw_pairs))
    sub_topics = ['google', 'amazon', 'microsoft', 'facebook', 'apple']
    pairs = []
    top_stem = []
    for pair in raw_pairs:
        details = pair.split()
        # stem_word = ps.stem(details[1])
        # if (pair,stem_word) not in top_stem:
        #     top_stem.append((pair,stem_word))
        pairs.append((details[0] + " " + details[1], details[2]))
    list.sort(pairs, key=lambda x: int(x[1]), reverse=True)
    print(pairs[:50])
    top_ten = pairs[:10]
    words_in_top = [word[0].split()[0] for word in top_ten]
    append_to_ten_words = [word for word in sub_topics if word not in words_in_top]
    append_to_ten = [pair for pair in pairs[:50] if pair[0].split()[0] in append_to_ten_words]
    top_ten[-len(append_to_ten):] = append_to_ten
    list.sort(top_ten, key=lambda x: int(x[1]), reverse=True)
    return top_ten

# scrap_data()


#all_words = wc_counts()
all_words = wc_counts()

print(all_words)
df = pd.DataFrame(columns = ['Source','Words','Count'])
for pair in all_words[:25]:
    result = {'Source':'CommonCrawl','Words': pair[0], 'Count':pair[1]}
    df = df.append(result, ignore_index=True)


print(df)
df.to_csv("cc_wc.csv", index= False)



# all_pairs = co_counts()
# print(all_pairs)
#
# for pair in all_pairs:
#     print(pair[0] + "**" + pair[1])

# all_pairs = co_counts_tw()
# print(all_pairs)
#
# for pair in all_pairs:
#     print(pair[0] + "**" + pair[1])