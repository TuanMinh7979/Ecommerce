// $() thi load css xong thi moi load css de load js ->nhanh


updateCartCountNumber();

function updateCartCountNumber() {
    $("#homeCartCounter").html(localStorage.getItem("productCounts") == undefined ? 0
        : localStorage.getItem("productCounts"));

}

// function updateListOrderNumber() {
//     $("#homeListOrderNumber").html(localStorage.getItem("ListOrderCount") == undefined ? 0
//         : localStorage.getItem("ListOrderCount"));
//
// }

// else load js xong thi load css-> cham






