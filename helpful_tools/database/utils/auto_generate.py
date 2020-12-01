import os
import utils
java_head = '''package team.combinatorics.shuwashuwa.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
'''


# 将csv文件转化成java类文件
def csv2javafile(srcdir, filename):
    # 将下划线命名转换为驼峰命名
    camel_name = utils.underscore_to_camel(filename[0:-4], is_class=True)
    print("生成java类：" + camel_name + '\n')
    # 生成的java文件
    java_name = camel_name
    java_file = open("java/" + java_name, 'w', encoding="utf-8")
    java_file.write(java_head)
    java_file.write("public class " + camel_name + ' {\n')
    csv_file = open(srcdir + filename, 'r', encoding="utf-8")
    csv_file.readline()
    for line in csv_file.readlines():
        # 读取csv信息
        tmp = line.lower().split(',')[0:2]
        # 写入成员变量信息
        java_file.write("    private {} {};\n".format(
            utils.sql_type_to_java_type(tmp[1]),
            utils.underscore_to_camel(tmp[0])))
    java_file.write('}')
    java_file.close()
    csv_file.close()


def init_sql_file():
    sql_file = open("sql/init.sql", 'w', encoding="utf-8")
    sql_file.write(
        'DROP DATABASE IF EXISTS `shuwashuwa`;\n' +
        'CREATE DATABASE `shuwashuwa` ' +
        'DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;\n' +
        'USE `shuwashuwa`;\n\n')
    sql_file.close()


def csv2sql(srcdir, filename):
    # 表名规范化
    table_name = filename[0:-4]
    if srcdir == 'relationship/':
        table_name = 'r_' + table_name

    # 读取CSV文件
    sql_file = open("sql/init.sql", 'a', encoding="utf-8")
    # 判断是否有表，如果有则drop，按理来说这里应该不会有表，因为初始化时如果数据库存在会重新建立一个数据库
    sql_file.write('DROP TABLE IF EXISTS `{}`;\n'.format(table_name))
    sql_file.write('CREATE TABLE `{}` (\n'.format(table_name))
    # 打开csv文件
    csv_file = open(srcdir + filename, 'r', encoding="utf-8")
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

    if len(unique_keys):
        sql_file.write(',\n')
        sql_file.write('    UNIQUE KEY (')

        for j in range(len(unique_keys)):
            sql_file.write('`{}`'.format(unique_keys[j]))
            if j != len(unique_keys) - 1:
                sql_file.write(',')
        sql_file.write(')')

    if len(normal_index):
        # 在上一行后补一个逗号
        sql_file.write(',\n')
        sql_file.write('    KEY `normalIndex` ({})'.format(
            str(normal_index)[1:-1].replace('\'', '`')))

    sql_file.write(
        '\n' +
        ') ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;\n'
        + '\n')

    print(table_name)
    sql_file.close()
    csv_file.close()


if __name__ == "__main__":
    os.chdir(os.path.dirname(os.path.dirname(__file__)))
    init_sql_file()
    for file in os.listdir('csv'):
        csv2javafile('csv/', file)
        csv2sql('csv/', file)
    for file in os.listdir('relationship'):
        csv2sql('relationship/', file)
    # print(underscore_to_camel("sdfsdf"))
