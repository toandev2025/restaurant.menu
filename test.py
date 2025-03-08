import requests
import time
import random
from datetime import datetime
import json

# URL của Google Form
url = "https://docs.google.com/forms/d/e/1FAIpQLSefidvlX4uqoCJ_c1yrWg0iKw2KdU_XeptoYhVhhWsi3pz5LQ/formResponse"

# Headers
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
    "Content-Type": "application/x-www-form-urlencoded",
    "Origin": "https://docs.google.com",
    "Referer": url,
    "Accept-Encoding": "gzip, deflate, br, zstd",
    "Accept-Language": "vi;q=0.7",
}

# Danh sách họ và tên
ho = ["Nguyen", "Tran", "Le", "Pham", "Hoang", "Huynh", "Vo", "Dang", "Bui"]
ten = ["Anh", "Binh", "Cuong", "Duong", "Ha", "Hung", "Huy", "Khanh", "Linh", "Minh", "Nam", "Phong", "Quan", "Thao", "Tu"]

# Danh sách đề xuất
de_xuat = [
"Bố trí thêm quạt hoặc máy lạnh để không gian mát mẻ hơn.",  
"Tăng tốc độ phục vụ vào giờ cao điểm để tránh chờ lâu.",  
"Có thể cân nhắc giảm giá cho sinh viên vào một số khung giờ thấp điểm.",  
"Bổ sung thêm chỗ ngồi cho những nhóm đông người."  
"Nâng cấp hệ thống đèn chiếu sáng để không gian căn tin sáng sủa hơn.",   
]

# Danh sách "Có"/"Không" random
yes_no_list = ["Có", "", "Không",]
yes_no_randomized = random.choices(yes_no_list, k=10)  # Random danh sách

# Danh sách 8 request không có đề xuất + 2 request có đề xuất (xáo trộn ngẫu nhiên)
has_opinion = [""] * 8 + [random.choice(de_xuat) for _ in range(2)]
random.shuffle(has_opinion)

# Danh sách giới tính cố định
gender_list = ["Nam"] * 6 + ["Nữ"] * 4
random.shuffle(gender_list)

# Hàm tạo email ngẫu nhiên
def random_email():
    username = f"{random.choice(ho).lower()}{random.choice(ten).lower()}{random.randint(90, 99)}"
    return f"{username}@gmail.com"

# Hàm tạo tên ngẫu nhiên
def random_name():
    ho_co_dau = ["Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Võ", "Đặng", "Bùi"]
    ten_co_dau = ["Anh", "Bình", "Cường", "Dương", "Hà", "Hùng", "Huy", "Khánh", "Linh", "Minh", "Nam", "Phong", "Quân", "Thảo", "Tú"]
    return f"{random.choice(ho_co_dau)} {random.choice(ten_co_dau)}"

# Hàm tạo mã số sinh viên
def random_student_id(year):
    year_prefix = {"Năm 1": "24", "Năm 2": "23", "Năm 3": "22", "Năm cuối": "21"}
    return year_prefix[year] + "".join([str(random.randint(0, 9)) for _ in range(8)])

# Hàm tạo payload ngẫu nhiên
def get_payload(request_index):
    # Random điểm số
    score_options = [2] * 1 + [3] * 3 + [4] * 4 + [5] * 5
    scores = {key: str(random.choice(score_options)) for key in [
        "entry.895533380", "entry.1509425024", "entry.1836949557", "entry.1339664410",
        "entry.1469022162", "entry.495006093", "entry.1091957922", "entry.1603723588",
        "entry.847662817", "entry.527276060", "entry.496615280", "entry.459014689",
        "entry.1363352637", "entry.922917518", "entry.1745452573", "entry.448142477"
    ]}

    # Giá trị "Có"/""/"Không"/"ko"/"Kó"
    co_khong = yes_no_randomized[request_index]

    # Năm học và giới tính random
    year = random.choice(["Năm 1", "Năm 2", "Năm 3", "Năm cuối"])
    gender = gender_list[request_index]

    # Lấy đề xuất (hoặc rỗng nếu không có)
    opinion = has_opinion[request_index]

    payload = {
        "entry.1299361926": co_khong,
        "entry.268565863": opinion,
        **scores,
        "entry.895533380_sentinel": "",
        "entry.1509425024_sentinel": "",
        "entry.1836949557_sentinel": "",
        "entry.1339664410_sentinel": "",
        "entry.1469022162_sentinel": "",
        "entry.495006093_sentinel": "",
        "entry.1091957922_sentinel": "",
        "entry.1603723588_sentinel": "",
        "entry.847662817_sentinel": "",
        "entry.527276060_sentinel": "",
        "entry.496615280_sentinel": "",
        "entry.459014689_sentinel": "",
        "entry.1363352637_sentinel": "",
        "entry.922917518_sentinel": "",
        "entry.1745452573_sentinel": "",
        "entry.448142477_sentinel": "",
        "fvv": "1",
        "partialResponse": f'[[[null,430926016,["Có"],0],[null,2047500574,["Có"],0],[null,816863396,["{random_email()}"],0],[null,1819556769,["{random_name()}"],0],[null,1639986564,["{gender}"],0],[null,1430788778,["{year}"],0],[null,1367766667,["{random_student_id(year)}"],0]],null,"4651690195854908569"]',
        "pageHistory": "0,1,2,3",
        "fbzx": "4651690195854908569",
        "submissionTimestamp": str(int(time.time() * 1000))
    }
    return payload

# Hàm gửi request
def send_request(payload, request_num):
    print(f"\n=== Request {request_num} ===")
    print(f"[{datetime.now()}] Đang gửi request...")

    try:
        response = requests.post(url, headers=headers, data=payload)
        if response.status_code == 200:
            print(f"[{datetime.now()}] Gửi thành công - Status Code: {response.status_code}")
        else:
            print(f"[{datetime.now()}] Lỗi - Status Code: {response.status_code}, Response: {response.text}")
    except Exception as e:
        print(f"[{datetime.now()}] Lỗi ngoại lệ: {str(e)}")

# Số lần gửi request
num_requests = 1

# Gửi request
for i in range(num_requests):
    payload = get_payload(i)
    send_request(payload, i + 1)

    # Thêm độ trễ ngẫu nhiên
    delay = random.uniform(1, 3)
    print(f"Đợi {delay:.2f} giây trước request tiếp theo...")
    time.sleep(delay)

print(f"[{datetime.now()}] Hoàn thành gửi {num_requests} request!")
