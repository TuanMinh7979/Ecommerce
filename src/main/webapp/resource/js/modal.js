const setTopModal = (topModalId, title, contentHtml) => {
    let modal = $("#" + topModalId);
    modal.find(".modal-title").text(title);
    modal.find(".modal-body").html(contentHtml);

}
const setCenterModal = (centerModelId, title, contentHtml) => {
    let modal = $("#" + c);
    modal.find(".modal-title").text(title);
    modal.find(".modal-body").html(contentHtml);

}