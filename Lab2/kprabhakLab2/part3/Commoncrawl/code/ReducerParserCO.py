def construct_dictionary(path):
    pairs = []
    for i in range(0,3):
        with open(path+ "//part-r-0000" +  str(i), "r", encoding="utf-8") as f:
            pairs.extend(f.readlines())
    return pairs


def co_counts(path):
    raw_pairs = construct_dictionary(path)
    print(len(raw_pairs))
    sub_topics = ['google', 'amazon', 'microsoft', 'facebook', 'apple']
    pairs = []
    for pair in raw_pairs:
        details = pair.split()
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


# Provide the path of the co occurrence reducer output folder
reducer_folder_path = "reducer_output_cc_co"
top_ten = co_counts(reducer_folder_path)
print(top_ten)