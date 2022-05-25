// $() thi load css xong thi moi load css de load js ->nhanh


updateCartCountNumber();
updateTransactionCount();

function updateCartCountNumber() {
    $("#homeCartCounter").html(localStorage.getItem("productCounts") == undefined ? 0
        : localStorage.getItem("productCounts"));

}

function updateTransactionCount() {
    $("#homeListOrderNumber").html(localStorage.getItem("transactionCount") == undefined ? 0
        : localStorage.getItem("transactionCount"));

}

// function updateListOrderNumber() {
//     $("#homeListOrderNumber").html(localStorage.getItem("ListOrderCount") == undefined ? 0
//         : localStorage.getItem("ListOrderCount"));
//
// }

// else load js xong thi load css-> cham






