# Software-Engineering
Introduction to Software Engineering

Database operation with Javafx
https://www.swtestacademy.com/database-operations-javafx/

UI with JavaFX
https://www.youtube.com/watch?v=cPF3qGTjYgk

Cách cài đặt:
1. Clone git
2. Vào File/Project Structure,
   1. Vào tab Project, Setup SDK version 11-18 (Nếu chưa có, search google "jdk 15" và tải phiên bản tương ứng với hệ điều hành)
   2. Cũng trong tab project, để Language level 15, out folder để mặc định
   3. Setup thư viện: Vào tab Libraries/ ấn vào dấu +/ Java.../chọn tất cả file jar của thư mục lib trong repo
      1. Linux thì thư mục lib là `lib`
      2. Window thì thư mục lib là `lib_window`
   4. Vào tab Modules, mark folder src trong repo là Sources folder (folder sẽ chuyển thành màu xanh)
3. Mở class DBUtil, di chuột vào chữ
   `import com.sun.rowset.CachedRowSetImpl;` màu đỏ, chọn add --export... theo gợi ý của Intellij
4. Vào File/Settings, tìm Java compiler, bỏ tích cái Use --release option, nhấn apply
5. Vào class LogIn, chuột phải rồi chọn `Run LogIn.main()`, sau đó edit lại run config và thêm biến VM như sau: 
    ```
    --module-path
   /home/hung/IdeaProjects/Software-Engineering/lib
   --add-modules  javafx.controls,javafx.fxml,javafx.media    
   --add-exports  javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED 
   --add-opens=java.sql.rowset/com.sun.rowset=ALL-UNNAMED
    ```
    (nhớ thay cái module path cho đúng với path của thư mục lib ở bước setup thư viện, code này là absolute path trong máy mình)
6. Apply và chạy lại, nhớ kết nối mạng vì database host trên cloud.
