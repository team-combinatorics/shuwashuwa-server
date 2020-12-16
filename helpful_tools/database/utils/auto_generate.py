import os
import utils
java_head = '''package team.combinatorics.shuwashuwa.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

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
    java_file.write("public class " + camel_name + 'PO' + ' {\n')
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


def complite_sql_file():
    sql_file = open("sql/init.sql", 'a', encoding="utf-8")
    sql_file.write('\nINSERT INTO user(openid, user_name, identity, is_su)\n' +
                   'VALUES (\'1da5505af2a5ba46a749eaa6b1a92003\'' +
                   ', \'shuwashuwa\', \'超级管理员\', 1);')
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
    normal_indexes = []
    unique_indexes = []
    primary_indexes = []
    fulltext_indexes = []

    lines = csv_file.readlines()
    for i in range(len(lines)):
        line = lines[i]
        tmp = line.strip().lower().split(',')
        sql_file.write('    `{}` {} {} COMMENT \'{}\''.format(
            tmp[0], tmp[1].upper(), tmp[2].upper(), tmp[3]))
        if i != len(lines) - 1:
            sql_file.write(',\n')
        if tmp[4] == '1':
            normal_indexes.append(tmp[0])
        if tmp[5] == '1':
            unique_indexes.append(tmp[0])
        if tmp[6] == '1':
            primary_indexes.append(tmp[0])
        if tmp[7] == '1':
            fulltext_indexes.append(tmp[0])
    # 写入普通索引
    # 普通索引名加idx前缀
    for n_idx in normal_indexes:
        sql_file.write(',\n    INDEX {}({})'.format('idx_' + n_idx, n_idx))
    # 写入唯一索引
    # 唯一索引名以uk为前缀
    for u_idx in unique_indexes:
        sql_file.write(',\n    UNIQUE {}({})'.format('uk_' + u_idx, u_idx))
    # 写入主键索引
    sql_file.write(',\n    PRIMARY KEY pk_id(`id`)')
    # 写入尾部信息
    sql_file.write(
        '\n' +
        ') ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;\n'
        + '\n')

    print(table_name)
    sql_file.close()
    csv_file.close()


if __name__ == "__main__":
    os.chdir(os.path.dirname(os.path.dirname(__file__)))
    if not os.path.exists('sql'):
        os.mkdir('sql')
    if not os.path.exists('java'):
        os.mkdir('java')
    init_sql_file()
    for file in os.listdir('csv'):
        csv2javafile('csv/', file)
        csv2sql('csv/', file)

    # for file in os.listdir('relationship'):
    #     csv2sql('relationship/', file)
    # print(underscore_to_camel("sdfsdf"))
    complite_sql_file()
