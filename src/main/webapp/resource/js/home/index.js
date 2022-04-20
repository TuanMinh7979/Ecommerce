$(function () {


    // simpleLoadContentWithGet("/api/category/21", renderProductFor);

})




function renderProductFor(data){
    let container = $("#hotsaleSwi").find(".swiper-wrapper");
    let rs = "";
    data.map(function (datai) {
        rs += `<div class="swiper-slide"><img src="${datai.mainImageLink}" alt=""></div>`
    })
    container.html(rs);
    createSwiper("#hotsaleSwi", 3)
}





