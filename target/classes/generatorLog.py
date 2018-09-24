# coding=UTF-8
import random
import time
url_paths = [
    "class/112.html",
    "class/128.html",
    "class/146.html",
    "class/145.html",
    "class/130.html",
    "class/131.html",
    "learn/821",
    "course/list"
]

ip_slices = [
    132, 134, 145, 156, 23, 45, 36, 52, 45, 234, 12
]
query_name = ["big data", "hadoop", "spark", "hive", "storm"]
search_engine = ["bing?q={query}", "google?q={query}", "baidu?q={query}", "yahoo?q={query}"]
status_code = [404, 500, 200]


def Sample_referer():
    if random.uniform(0, 1) > 0.3:
        return "-"
    search = random.sample(search_engine, 1)[0]
    query1 = random.sample(query_name, 1)[0]
    referer = search.format(query=query1)
    return referer


def generate_log(count=10):
    # https: // www.imooc.com / search / course?words = docker
    curtime = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    f = open("/root/data/generateLog", "w+")
    while count >= 0:
        log1 = "{ip}\t{curtime}\t\"GET {url} HTTP/1.1\"\t{status}\t{referer}".format(url=random.sample(url_paths, 1)[0],
                                                                                     ip=".".join([str(slice) \
                                                                                                  for slice in
                                                                                                  random.sample(
                                                                                                      ip_slices, 4)]),
                                                                                     referer=Sample_referer(), \
                                                                                     status=
                                                                                     random.sample(status_code, 1)[0],
                                                                                     curtime=curtime)

        f.write(log1 + "\n")
        count = count - 1


if __name__ == '__main__':
    generate_log(5)
