# back_end_for_file_storing_android
A small school project for saving file on server  
Làm fe ở FeAndroid  
Không động vào main activity vì chứa code gọi api
###Luồng hoạt động của app
- Sau khi đăng nhập sẽ đưa người dùng vào tab thư mục
- Trước đó, api sẽ được gọi và lấy ra tên các thành phần trong tên người dùng và hiển thị lên màn hình
- Tab ảnh sẽ lấy ra các ảnh
- Tab video sẽ lấy ra các video
- Tab file sẽ lấy ra các loại file không thuộc 2 cái trên
- Khi bấm vào 1 file, api sẽ được gọi và tải file đó về trên máy rồi gọi 1 app khác để hiển thị
- Khi bấm vào 1 thư mục, tên các thành phần trong thư mục sẽ được tải về và hiển thị
- Nếu chọn tải file hay thư mục, ta sẽ chọn 1 thư mục và tải ảnh lên, sau đó, tên các thành phần trong thư mục sẽ được tải về và hiển thị
- Các chức năng chưa được phát triển: Xóa file, xóa thư mục, di chuyển file, di chuyển thư mục, chia sẻ file, chia sẻ thư mục?
