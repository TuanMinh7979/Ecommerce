function createMultiSlideSwi(selector, numOfSlide) {
    let swiper = new Swiper(selector, {
        slidesPerView: numOfSlide,
        spaceBetween: 30,
        slidesPerGroup: numOfSlide,
        loop: true,
        loopFillGroupWithBlank: true,
        pagination: {
            // el: ".swiper-pagination",
            clickable: true,
        },
        lazyLoading: true,
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });
    return swiper;
}

function createSomeSlideSwi(selector) {

    let swiper = new Swiper(selector, {
        pagination: {
            el: ".swiper-pagination",
            type: "progressbar",
        },
        lazyLoading: true,
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });
    return swiper;
}


//load content through ajax

function ajaxGet(url, renderfunction) {
    $.ajax({
        type: "get",
        url: url,
        success: function (data) {
            if (typeof renderfunction === "function") {
                renderfunction(data);
            }

        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load content',
                // text: 'Something went wrong!',

            })
        }


    });

}