<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>继续等待</title>
    <script src="https://cdn.bootcss.com/limonte-sweetalert2/7.21.1/sweetalert2.all.min.js"></script>
</head>
<body>
    <script>
        swal({
            title: "正在创建订单，请稍候...",
            timer : 3000,
            confirmButtonText: "刷新",
            type: "info"
        }).then(function (result) {
            console.log("别闹了")
            window.location.href = "/checkorder?orderNo=${orderNo}";
        });
    </script>
</body>
</html>