import os
java_head = '''package team.combinatorics.shuwashuwa.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
'''
class_dic = {
    'int': 'int',
    'varchar': 'String',
    'text': 'String',
    'tinyint': 'int',
    'date': 'String',
    'datetime': 'String',
    'boolean': 'boolean'
}


def xhx2tf(name):
    wordsList = name.split('_')
    print(wordsList)
    rst = ''
    for word in wordsList:
        rst += word[0].upper() + word[1:]
    return rst


# 将csv文件转化成java类文件
def csv2javafile(filename):
    # 将下划线命名转换为驼峰命名
    tf_name = xhx2tf(filename[0:-4])
    print("生成java类：" + tf_name + '\n')
    # 生成的java文件
    java_name = tf_name + '.java_tmp'
    java_file = open("java/" + java_name, 'w', encoding="utf-8")
    java_file.write(java_head)
    java_file.write("public class " + tf_name + ' {\n')
    csv_file = open("csv/" + filename, 'r', encoding="utf-8")
    csv_file.readline()
    for line in csv_file.readlines():
        tmp = line.lower().split(',')[0:2]
        java_file.write("    private ")
        if tmp[1].startswith("varchar"):
            java_file.write("String " + tmp[0] + ';\n')
            continue
        java_file.write(class_dic[tmp[1]] + ' ' + tmp[0] + ';\n')
    java_file.write('}')
    java_file.close()
    csv_file.close()


def init_sql_file():
    sql_file = open("sql/init.sql", 'w', encoding="utf-8")
    sql_file.close()


def csv2sql(filename):
    table_name = filename[0:-4]
    sql_file = open("sql/init.sql", 'a', encoding="utf-8")
    sql_file.write('DROP TABLE IF EXISTS `{}`;\n'.format(table_name))
    sql_file.write('CREATE TABLE `{}` (\n'.format(table_name))
    csv_file = open("csv/" + filename, 'r', encoding="utf-8")
    csv_file.readline()
    normal_index = []
    unique_keys = []
    lines = csv_file.readlines()
    for i in range(len(lines)):
        line = lines[i]
        tmp = line.strip().lower().split(',')
        sql_file.write('    `{}` {} {} COMMENT \'{}\''.format(
            tmp[0], tmp[1].upper(), tmp[3].upper(), tmp[2]))
        if i != len(lines) - 1:
            sql_file.write(',\n')
        if tmp[4] == '1':
            normal_index.append(tmp[0])
        if tmp[5] == '1':
            unique_keys.append(tmp[0])
        print(tmp)

    if len(unique_keys):
        sql_file.write(',\n')
        sql_file.write('    UNIQUE KEY (')
        for j in range(len(unique_keys)):
            sql_file.write('`{}`'.format(unique_keys[j]))
            if j != len(unique_keys) - 1:
                sql_file.write(',')
        sql_file.write(')')

    if len(normal_index):
        sql_file.write(',\n')
        sql_file.write('    KEY `normalIndex` (')
        for j in range(len(normal_index)):
            sql_file.write('`{}`'.format(normal_index[j]))
            if j != len(normal_index) - 1:
                sql_file.write(',')
        sql_file.write(')')
    sql_file.write('\n')
    sql_file.write(
        ') ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;\n'
        + '\n')
    print(table_name)
    sql_file.close()
    csv_file.close()


if __name__ == "__main__":
    os.chdir(os.path.dirname(__file__))
    init_sql_file()
    for file in os.listdir('csv'):
        csv2javafile(file)
        csv2sql(file)
    # for file in os.listdir('merge'):
    #     csv2sql(file)
