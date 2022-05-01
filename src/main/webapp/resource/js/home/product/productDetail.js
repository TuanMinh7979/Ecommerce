$(function () {
    ajaxGet(`/ajax/product/${productId}` + "/images", renderProductImagesSlide);
    ajaxGet(`/admin/product/api/${productId}` + "/attributes", renderProductDetail);
})

function renderProductDetail(data) {
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