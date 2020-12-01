import random

headers = []
types = []
items = []

num_random = 20
num_sudo = 3


def generate_str(length):
    string = []
    for s in range(length):
        s = chr(random.randint(36, 120))
        if s != ',':
            string.append(s)
    return ''.join(string)


def generate_level():
    return []


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

    with open('../csv/user_init.csv', 'w', encoding="utf-8") as f:
        for idx in range(len(headers)):
            f.write(headers[idx]+',')

            if headers[idx] in ['id', 'create_time', 'updated_time']:
                for cnt in range(num_sudo+num_random):
                    f.write(',')
            elif types[idx] == "BOOLEAN":
                for cnt in range(num_sudo):
                    f.write('1,')
                for cnt in range(num_random):
                    f.write('0,')
            else:
                if headers[idx] == 'user_name':
                    f.write('kinami,misaki,leesou,')
                    for _ in range(num_random):
                        name = generate_str(5)
                        f.write((name+','))
                else:
                    for _ in range(num_sudo+num_random):
                        name = generate_str(random.randint(5, 10))
                        f.write((name+','))

            f.write('\n')


if __name__ == '__main__':
    generate_csv('../ref_csv/user.csv')

