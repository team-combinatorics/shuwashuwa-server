import os
import utils


def csv2mapper(srcdir, filename):
    class_name = utils.underscore_to_camel(filename[0:-4], True)
    pojo_name = utils.underscore_to_camel(filename[0:-4])
    mapper_name = class_name + 'Mapper'
    result_map_name = utils.underscore_to_camel(filename[0:-4]) + 'ResultMap'
    mapper_file = open('mapper/' + mapper_name, 'w', encoding="utf-8")
    # 写入开头的信息
    mapper_file.write(
        '<?xml version="1.0" encoding="UTF-8"?>\n' +
        '<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" ' +
        '"http://mybatis.org/dtd/mybatis-3-mapper.dtd">\n\n')
    dao_name = class_name + 'Dao'
    # Mapper标签
    mapper_file.write(
        '<mapper namespace="team.combinatorics.shuwashuwa.dao.{}">\n'.format(
            dao_name))
    # resultMap标签
    mapper_file.write(
        '    <resultMap type=' +
        '\"team.combinatorics.shuwashuwa.model.pojo.{}\" id=\"{}\">\n'.format(
            class_name + 'DO', result_map_name))
    # 读取csv信息
    csv_file = open(srcdir + filename, 'r', encoding="utf-8")
    property_names = []
    columns = []
    # 丢弃第一行
    csv_file.readline()
    for line in csv_file.readlines():
        tmp = line.lower().split(',')[0:2]
        column = tmp[0].lower()
        columns.append(column)
        class_property = utils.underscore_to_camel(column)
        property_names.append(class_property)
        jdbcType = utils.get_sql_type(tmp[1])
        mapper_file.write(
            '        <result column="{}" jdbcType="{}" property="{}" />\n'.
            format(column, jdbcType, class_property))
    # 关闭csv文件
    csv_file.close()

    # 关闭resultMap标签
    mapper_file.write('    </resultMap>\n\n')

    # 生成通用的update语句
    mapper_file.write(
        '    <update id="update" ' +
        'parameterType="team.combinatorics.shuwashuwa.model.pojo.{}">\n'.
        format(class_name + 'DO'))
    mapper_file.write('        update ' + filename[0:-4] + '\n')
    # begin trim
    mapper_file.write('        <trim prefix="SET" suffixOverrides=",">\n')
    # 添加查询语句s
    for i in range(len(property_names)):
        mapper_file.write('            <if test="{}.{} != null">\n'.format(
            pojo_name, property_names[i]))
        mapper_file.write(' ' * 16 + '{} = #{{{}.{}}},\n'.format(
            columns[i], pojo_name, property_names[i]))
        mapper_file.write('            </if>\n')
    # end trim
    mapper_file.write('        </trim>\n')
    # where 语句
    mapper_file.write('        where id = #{{{}.id}}\n'.format(pojo_name))
    mapper_file.write('    </update>\n')
    # 关闭mapper标签
    mapper_file.write('</mapper>\n')

    # 关闭mapper文件
    mapper_file.close()
    print(result_map_name)


if __name__ == "__main__":
    os.chdir(os.path.dirname(os.path.dirname(__file__)))
    for file in os.listdir('csv'):
        csv2mapper('csv/', file)
