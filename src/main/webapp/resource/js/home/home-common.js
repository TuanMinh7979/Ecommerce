function createSwiper(selector, numOfSlide) {
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



//load content through ajax