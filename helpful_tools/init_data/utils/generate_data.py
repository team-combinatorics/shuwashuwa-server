import random

headers = []
types = []

num_random = 20
num_sudo = 3
sudo_dict = {
    'openid': 'kinami,misaki,leesou,',
    'user_name': 'kinami,misaki,leesou,',
    'nick_name': 'kinami,misaki,leesou,',
    'phone_number': '114514,1919810,1145151919810,',
    'email': 'shuwashuwa@kinami.cc,shuwashuwa@kinami.cc,shuwashuwa@kinami.cc,',
    'identity': 'chairman,chairman,chairman,',
    'department': 'eecs,eecs,eecs,',
    'grade': '3,3,3,',
    'student_id': '114,514,1919,',
    'comment': 'happy,lucky,smile yeah!,'
}


def generate_str(length):
    string = []
    for s in range(length):
        s = chr(random.randint(65, 90))
        string.append(s)
    return ''.join(string)


def generate_level():
    vo = []
    ad = []
    su = []
    for _ in range(num_random):
        s = chr(random.randint(ord('0'), ord('1')))
        vo.append(s)
        if s == '0':
            ad.append('0')
            su.append('0')
        else:
            s = chr(random.randint(ord('0'), ord('1')))
            ad.append(s)
            if s == '0':
                su.append('0')
            else:
                s = chr(random.randint(ord('0'), ord('1')))
                su.append(s)
        vo.append(',')
        ad.append(',')
        su.append(',')
    level_dict = {
        'is_volunteer': ''.join(vo),
        'is_admin': ''.join(ad),
        'is_su': ''.join(su)
    }
    return level_dict


def generate_csv(filename):
    with open(filename, 'r', encoding="utf-8") as f:
        f.readline()
        for line in f.readlines():
            line = line.strip().split(',')
            headers.append(line[0])
            types.append(line[1])
    print(headers)
    print(types)
    random_level = generate_level()
    print(random_level)

    with open('../csv/user_init.csv', 'w', encoding="utf-8") as f:
        for idx in range(len(headers)):
            f.write(headers[idx] + ',')

            if headers[idx] in ['id', 'create_time', 'updated_time']:
                for cnt in range(num_sudo + num_random):
                    f.write(',')
            elif types[idx] == "BOOLEAN":
                for cnt in range(num_sudo):
                    f.write('1,')
                f.write(random_level[headers[idx]])
            else:
                f.write(sudo_dict[headers[idx]])
                for _ in range(num_random):
                    name = generate_str(random.randint(5, 10))
                    f.write((name + ','))
            f.write('\n')


if __name__ == '__main__':
    generate_csv('../ref_csv/user.csv')
