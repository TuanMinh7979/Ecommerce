// $() thi load css xong thi moi load css de load js ->nhanh

$(function(){
    $("#homeCartCounter").html(localStorage.getItem("productCounts") == undefined ? 0
        : localStorage.getItem("productCounts"));


})
// else load js xong thi load css-> cham






