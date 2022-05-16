$(function () {
    ajaxGet(`/ajax/product/${productId}` + "/images", renderProductImagesSlide);
    ajaxGet(`/ajax/product/${productId}` + "/attributes", renderProductDetail);


})

function renderProductDetail(data) {
    // console.log(data);
    let container = $("#productSpec");
    let rs = "";
    let dataObj = {};
    dataObj = JSON.parse(data);

    for (let atri in dataObj) {
        rs += `<tr> <td>${atri}</td> <td>${dataObj[atri].value}</td></tr>`

    }


    container.html(rs);
}

function renderProductImagesSlide(data) {
    let container = $("#productDetailImageWrapper").find(".swiper-wrapper");
    let rs = "";
    data.map(function (datai) {

        rs += `<div class="swiper-slide"><img src="${datai}"/></div>`
    })
    container.html(rs);
    const productImagesSlide = createSomeSlideSwi("#productDetailImageWrapper");

}

let count = 0;
let pricei = $("#pPriceSpan").text();
$("#buybtn").on("click", function () {

    $("#showBuyModalBtn").click();
})

$("#upbtn").click(function (e) {
    e.preventDefault();
    count++;
    $("#countInp").val(count);
    $("#pPriceSpan").text(pricei * count);

});
$("#downbtn").click(function (e) {
    e.preventDefault();
    count--;
    if (count < 1) {
        count = 1;
    }
    $("#countInp").val(count);
    $("#pPriceSpan").text(pricei * count);
});

$('#addToCartModalBody__Submit-Btn').click(function (e) {
    e.preventDefault();
    if (localStorage.getItem("productCounts") == undefined) {
        localStorage.setItem("productCounts", count);

    } else {
        localStorage.setItem("productCounts", parseInt(localStorage.getItem("productCounts")) + parseInt(count));
    }

    if (localStorage.getItem("cartObj") == undefined) {
        let newProduct = {};
        newProduct["id"] = $("#pIdInp").val();


    } else {

    }


    alert("New product is added to cart!");
    $("#homeCartCounter").html(localStorage.getItem("productCounts"));


});






