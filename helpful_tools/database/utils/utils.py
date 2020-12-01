class_dic = {
    'int': 'Integer',
    'varchar': 'String',
    'text': 'String',
    'tinyint': 'Integer',
    'datetime': 'Timestamp',
    'date': 'Date',
    'boolean': 'boolean',
    'int unsigned': 'Integer'
}


def sql_type_to_java_type(type_name: str) -> str:
    if type_name.lower().startswith('varchar'):
        return 'String'
    return class_dic[type_name]


def get_sql_type(type_name: str) -> str:
    for key in class_dic.keys():
        if type_name.lower().startswith(key):
            if key == 'int':
                return 'INTEGER'
            if key == 'text':
                return 'LONGVARCHAR'
            if key == 'datetime':
                return 'TIMESTAMP'
            return key.upper()


def underscore_to_camel(name, is_class=False):
    # 以下划线为分隔切词
    wordsList = name.split('_')
    # print(wordsList)
    result = ''
    for word in wordsList:
        # 对布尔型的命名不要加is
        if word == 'is' and not is_class:
            continue
        result += word[0].upper() + word[1:]
    if not is_class:
        result = result[0].lower() + result[1:]
    return result


if __name__ == "__main__":
    print('asdfasfasd'[-2:])
