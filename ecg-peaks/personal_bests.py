import pandas as pd


def personal_bests(df):
    groups = df.groupby(['exer'], sort = False)

    list1, list2, list3 = ([] for i in range(3))

    for exer, group_df in groups:
        max_weight = group_df['weight'].max()
        max_weight_reps = group_df.loc[group_df['weight'] == max_weight]['reps'].max()

        more_reps = group_df.loc[(group_df['exer'] == exer) & (group_df['reps'] > max_weight_reps)].groupby('reps')

        list_exer, list_rep, list_weight = ([] for i in range(3))

        for rep, reps_df in more_reps:
            max_weight_this_rep = reps_df['weight'].max()

            for wt in range(len(list_weight) - 1, -1, -1):
                if max_weight_this_rep >= list_weight[wt]:
                    del list_exer[wt]
                    del list_weight[wt]
                    del list_rep[wt]
            list_exer.append(exer)
            list_weight.append(max_weight_this_rep)
            list_rep.append(rep)

        list1 += [exer] + list_exer
        list2 += [max_weight] + list_weight
        list3 += [max_weight_reps] + list_rep

    final_df = pd.DataFrame({'exer': list1, 'weight': list2, 'reps': list3})

    return final_df

