//function submitRating(value) {
//    // Check if the user has already submitted a rating
//    var hasRated = document.getElementById('selectedRating').textContent.trim() !== '';
//
//    if (!hasRated) {
////        var confirmation = confirm('Bạn có chắc đã đánh giá ' + value + ' sao cho bộ phim này?');
////
////        if (confirmation) {
////            document.getElementById('selectedRating').textContent = value + ' star(s)';
////
////            // Send the rating to the server using AJAX
////            var xhr = new XMLHttpRequest();
////            xhr.open("POST", "/ratings", true);
////            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
////            xhr.send("value=" + value);
////
////            // Display an alert message after successful rating
////            alert('Đánh giá thành công!');
//
//        var confirmation = confirm('Bạn có chắc đã đánh giá ' + value + ' sao cho bộ phim này?');
//
//        if (confirmation) {
//            document.getElementById('selectedRating').textContent = value + ' sao';
//
//            // Send the rating to the server using Fetch API
//            fetch('/ratings', {
//                method: 'POST',
//                headers: {
//                    'Content-Type': 'application/x-www-form-urlencoded',
//                },
//                body: 'value=' + value,
//            })
//            .then(response => {
//                if (!response.ok) {
//                    throw new Error('Network response was not ok');
//                }
//
//                fetch('/ratings', {
//                    method: 'GET'
//                }).then(res => console.log('calling get request'))
//
//                // Display an alert message after successful rating
//                alert('Đánh giá thành công!');
//            })
//            .catch(error => {
//                // Handle errors here
//                console.error('There was a problem with the fetch operation:', error);
//            });
//        }
//
//        }
//    } else {
//        alert('Bạn đã đánh giá rồi!');
//    }
//}