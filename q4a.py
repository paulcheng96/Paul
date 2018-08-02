import pandas as pd
from sklearn.preprocessing import normalize
from numpy import *

def via_distance(vector1,vector2):
    cosV12 = dot(vector1,vector2)/(linalg.norm(vector1)*linalg.norm(vector2))
    return cosV12

question_words = pd.read_csv(r'F:\nlp_homework\data_input\questions-words.txt')
question_words_list = []
for i in question_words.values:
    for j in i:
        question_words_list.extend(str(j).split(' '))

question_words_list = list(set(question_words_list))
question_words_list = [str(word).lower() for word in question_words_list]

glove_vectors = pd.read_csv(r'F:\nlp_homework\data_input\glove.6B.50d.txt',sep=' ',header=None,index_col=0)
glove_vectors = glove_vectors.reset_index()
glove_vectors = glove_vectors[glove_vectors[0].isin(question_words_list)]
glove_vectors = glove_vectors.set_index(0)
glove_index_words = list(glove_vectors.index)
glove_vectors = normalize(glove_vectors,norm='l2',axis=1)
glove_vectors = pd.DataFrame(glove_vectors,index=glove_index_words)

total_count = 0
closet_count = 0
five_close_count = 0

for words_list in question_words.values:
    if str(words_list).__contains__(':'):
        pass
    else:
        words_list = [str(word).lower() for word in   str(words_list[0]).split(' ')]

        if (words_list[0] in glove_index_words) & (words_list[1] in glove_index_words) & \
           (words_list[2] in glove_index_words) & (words_list[3] in glove_index_words):

            print (words_list)
            x_a = glove_vectors.loc[words_list[0],:]
            x_b = glove_vectors.loc[words_list[1],:]
            x_c = glove_vectors.loc[words_list[2],:]
            x_d = glove_vectors.loc[words_list[3],:]
            y = x_b - x_a + x_c

            distace_list = []
            for i in range(len(glove_vectors)):
                temp_dis = via_distance(glove_vectors.iloc[i,:],y)
                distace_list.append((temp_dis))

            five_closest_vector = []
            for i in range(5):
                max_value = max(distace_list)
                five_closest_vector.append(max_value)
                distace_list.remove(max_value)

            x_d_distance_y = via_distance(x_d,y)

            print (five_closest_vector,x_d_distance_y)

            total_count += 1
            if x_d_distance_y >= five_closest_vector[0]:
                closet_count += 1
            if x_d_distance_y >= five_closest_vector[4]:
                five_close_count += 1


score_closest = closet_count*1.0/total_count
score_five_closest = five_close_count*1.0/total_count
print ("glove_model",score_closest,score_five_closest)


counter_fitted_vectors = pd.read_csv(r'F:\nlp_homework\data_input\counter-fitted-vectors.txt',sep=' ',header=None,index_col=0)
counter_fitted_vectors = counter_fitted_vectors.reset_index()
counter_fitted_vectors = counter_fitted_vectors[counter_fitted_vectors[0].isin(question_words_list)]
counter_fitted_vectors = counter_fitted_vectors.set_index(0)
counter_fitted_index_words = list(counter_fitted_vectors.index)
counter_fitted_vectors = normalize(counter_fitted_vectors,norm='l2',axis=1)
counter_fitted_vectors = pd.DataFrame(counter_fitted_vectors,index=counter_fitted_index_words)

total_count = 0
closet_count = 0
five_close_count = 0

for words_list in question_words.values:
    if str(words_list).__contains__(':'):
        pass
    else:
        words_list = [str(word).lower() for word in   str(words_list[0]).split(' ')]

        if (words_list[0] in counter_fitted_index_words) & (words_list[1] in counter_fitted_index_words) & \
           (words_list[2] in counter_fitted_index_words) & (words_list[3] in counter_fitted_index_words):

            print (words_list)
            x_a = counter_fitted_vectors.loc[words_list[0],:]
            x_b = counter_fitted_vectors.loc[words_list[1],:]
            x_c = counter_fitted_vectors.loc[words_list[2],:]
            x_d = counter_fitted_vectors.loc[words_list[3],:]
            y = x_b - x_a + x_c

            distace_list = []
            for i in range(len(counter_fitted_vectors)):
                temp_dis = via_distance(counter_fitted_vectors.iloc[i,:],y)
                distace_list.append((temp_dis))

            five_closest_vector = []
            for i in range(5):
                max_value = max(distace_list)
                five_closest_vector.append(max_value)
                distace_list.remove(max_value)

            x_d_distance_y = via_distance(x_d,y)

            print (five_closest_vector,x_d_distance_y)

            total_count += 1
            if x_d_distance_y >= five_closest_vector[0]:
                closet_count += 1
            if x_d_distance_y >= five_closest_vector[4]:
                five_close_count += 1


score_closest = closet_count*1.0/total_count
score_five_closest = five_close_count*1.0/total_count
print ("counter_model",score_closest,score_five_closest)

