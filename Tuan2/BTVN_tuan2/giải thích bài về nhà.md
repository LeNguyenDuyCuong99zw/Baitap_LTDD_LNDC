Trong bài này, code tạo giao diện nhập họ tên và tuổi, rồi phân loại theo độ tuổi. Hai ô nhập dùng TextField điều khiển bởi TextEditingController. Khi bấm nút “Kiểm tra”, hàm \_checkPerson() chạy. Trong hàm này, chương trình lấy giá trị
nhập vào bằng trim() để bỏ khoảng trắng, rồi dùng int.tryParse() đổi chuỗi thành số. Nếu ô trống hoặc dữ liệu sai,
if sẽ báo lỗi bằng biến \_error. Nếu hợp lệ, chương trình dùng nhiều if…else if để xét độ tuổi

Trên 65 → Người già
Từ 6–65 → Người lớn
Từ 2–5 → Trẻ em
Dưới 2 → Em bé

Kết quả được gán cho \_result và hiển thị ra màn hình. Code cũng có setState() để cập nhật giao diện
