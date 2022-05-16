var mode = "";
var attributesObject = {};

var numOfImage = 1;
var MAX_FILE_SIZE = 512000;

var imageChgFlag = "0";
var colorImageChgFlag = "0";

var currentColorPickerImgId = 0;

$(function () {
    callCategoriesData();

    $("#shorDescription").richText();
    $("#fullDescription").richText();
    if (document.getElementById("productId") != null) {
        mode = "edit";
    }

    $("#selectnone-btn").on("click",
        function (event) {
            event.preventDefault();
            handleSelectDefaultBtn(this, mode, "delImageIds", defaultImage);
        })


})

//Color picker
$(document).on("click", ".colorpicker__span", function () {
    currentColorPickerImgId = $(this).closest(".img-wrapper").attr('id');
    console.log(currentColorPickerImgId);
    $("#launch-cp-modal").modal("show");

})

$(".color-item").on("click", function () {
    let colorText = $(this).text();

    $("#" + currentColorPickerImgId).find(".color-text").val(colorText);
    if (colorImageChgFlag == "0") {
        colorImageChgFlag = "1";
    }

    $("#launch-cp-modal").modal("hide");
});


$("#launch-cp-modal").on("hidden.bs.modal", function () {
    currentColorPickerImgId = 0;
});
///Color picker


$(document).on("change", ".file_inp", function () {
    if (imageChgFlag == "0") {
        imageChgFlag = "1";
    }
})
$(document).on("change", ".color-text", function () {
    if (colorImageChgFlag == "0") {
        colorImageChgFlag = "1";
    }
})

$('#mainForm').submit(function () {
    let flags = imageChgFlag + colorImageChgFlag;
    if (flags !== "00") {
        $(this).append('<input type="hidden" name="flags" value="' + flags + '" />');
    }
    // alert(flags)
    return true;
});

//CALL CATEGORIES FOR FORM
function callCategoriesData() {
    $.ajax({
        type: "get",
        url: "/ajax/hierarchical-category",
        success: function (data) {

            renderCatHierarchical(data);
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not load this categories in hierarchical',
                // text: 'Something went wrong!',

            })
        }
    });
}

function renderCatHierarchical(data) {
    let opts = "";
    if (mode == "edit") {
        data.map(function (cat) {
            if (cat.id != productCategoryId) {
                opts += `<option value="${cat.id}">${cat.name}</option> `

            } else {
                opts += `<option value="${cat.id}" selected>${cat.name}</option> `
            }
        })
    } else {
        data.map(function (cat) {
            opts += `<option value="${cat.id}" >${cat.name}</option> `

        })
    }


    $("#category-sel").append(opts);

}

//CALL CATEGORIES FOR FORM


$(document).on("change", ".file_inp", function (event) {
    event.preventDefault();
    changeImage(this.files[0], this, mode, "delImageIds", defaultImage)

});


function createNewEmptyExtraImage() {


    event.preventDefault();
    let html = '       <div class="img-wrapper col-6">\n' +
        '\n' +
        '<span>Extra</span>    <span class="float-right image-color-picker">' +
        '<input name="extraColor" type="text" class="color-text" value="no color" >' +
        '<span class="colorpicker__span"><i class="colorpicker__i fas fa-eye-dropper"></i></span>' +
        '</span>' +
        '            <div class="image-preview">\n' +
        '                <i class="close-i fas fa-times"></i>\n' +
        `                <img  src="${defaultImage}" alt="alt" class="image-preview__img"/>\n` +
        '\n' +
        '\n' +
        '            </div>\n' +
        '            <input type="file" name="files" class="file_inp"/>\n' +
        '        </div>'


    $("#imageWrapper").append(html);


}


$(document).on("click", "#addNewImageExtraBtn", createNewEmptyExtraImage);


$(document).on("click", ".image-preview .close-i", function () {
    // alert(productId);
    if ($(this).parent().hasClass("saved-image-preview") && mode === "edit") {
        let oldIds = document.getElementById("delImageIds").value.trim();
        document.getElementById("delImageIds").value = oldIds + " " + this.parentElement.parentElement.id + " ";

    }


    $(this).parent().parent().remove();
})

//DETAIL ATTRIBUTE TAB
$(function () {

    $('.nav-tabs a').on('shown.bs.tab', function (event) {
        var editMode = $(event.target).text();

        if (editMode == "Detail") {

            callAttributeApi(`/admin/product/api/${productId}/attributes`);
            $("#addNewAttributeBtn").on("click", function (event) {
                event.preventDefault();
                $("#addModalBtn").click();
            })

            $(".saveAtrsBtn").on("click", function (event) {
                event.preventDefault();
                saveCurrentFormAttribute(this);

            });

            $("#tag_delete_many").on("click", function (event) {

                event.preventDefault();
                let checkboxes = $('tr input[type="checkbox"]:checked');
                for (checkbox of checkboxes) {
                    let nameToDel = $(checkbox).parent().parent().find("td:nth-child(2)").text();
                    removeAttributeByName(nameToDel, attributesObject);
                    $(checkbox).closest("tr").remove();

                }
            });
            $(document).on("click", "#saveallchangeBtn", function (event) {
                event.preventDefault();
                saveAllChange(attributesObject, $(this).attr("href"));

            })


            $(document).on("click", '.tag_delete_one', function (event) {
                event.preventDefault();
                let nameToDel = $(this).parent().parent().find("td:nth-child(2)").text();
                removeAttributeByName(nameToDel, attributesObject);
                $(this).closest("tr").remove();
            })


        }


    });

})


function saveAllChange(data, url) {

    $.ajax({
        type: "post",
        url: url,
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function (res) {
            location.reload();
        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not call this Api',
                // text: 'Something went wrong!',

            })
        }


    });
}


$(document).on("click", ".push", function (event) {

    event.preventDefault();
    pushNewRecord(this)


});
$(document).on("click", ".delete", function (event) {

    event.preventDefault();
    $(this).parent().remove();
});


$(document).on("click", ".editAttributeBtn", function (event) {
    event.preventDefault();
    //load data first
    loadAttributeToForm(this);
    //after that click modal btn
    $("#editModalBtn").click();

})

function renderDataForAttributeTable(data) {
    let rs = "";


    for (let atbi in data) {
        let curAtr = data[atbi];
        rs += `       <tr class="col-12">
                <td class="col-1"><input type="checkbox" class="atb-iddel-checkbox" value="${atbi}"></td>
                <td class="col-5" class="atb-name-inp" ">${atbi}</td>
                <td class="col-3"><i class="atb-active-checkbox fas fa-circle" isactive="${curAtr.active}"></i>  </td>
                <td class="col-3">
                    <button class="editAttributeBtn btn btn-default" >Edit</button>
                    <a class="btn btn-danger tag_delete_one"   >Delete</a>

                </td>

            </tr>`
    }
    // }

    $("#tabledata").html(rs);
    setActiveCheckbox();


}

function callAttributeApi(url) {

    $.ajax({
        type: "get",
        url: url,
        success: function (data) {
            if (data !== "" && data !== null && data !== undefined) {
                attributesObject = JSON.parse(data);
                renderDataForAttributeTable(attributesObject);
            } else {

                alert("This category do not have any attribute");
            }

        },
        error: function () {
            Swal.fire({
                icon: 'error',
                title: 'Can not call this Api',
                // text: 'Something went wrong!',

            })
        }


    });
}

//-DETAIL ATTRIBUTE TAB

//COMMON METHOD
function loadAttributeToForm(editAttributeBtn) {
    let tri = $(editAttributeBtn).parent().parent();
    let triKeyName = tri.find(".atb-iddel-checkbox").val();
//
    //-
    let isactive = tri.find(".atb-active-checkbox").attr("isactive");

    let editModal = $("#exampleModalCenter2");
    editModal.find(".atrNameInp").val(triKeyName);
    editModal.find(".atrActiveChbx").prop("checked", isactive == 1 ? true : false);

    let valueList = editModal.find(".tasks");

    let currentAttribute = attributesObject[triKeyName];

    if (currentAttribute["value"] != undefined) {
        let rs = "";
        currentAttribute["value"].map(function (valuei) {
            rs +=
                `<div class="task">
                <span class="atrValueSpan" class="taskname">
                    ${valuei}
                </span>
                        <button class="delete">
                            <i class="far fa-trash-alt"></i>
                        </button>
                    </div>`;


        });
        valueList.html(rs);
    }
}


function saveCurrentFormAttribute(saveFormBtn) {


    let data = {}
    let modalContent = $(saveFormBtn).parent().parent();
    let attributeWrapper = modalContent.find(".attributeWrapper");

    let keyname = attributeWrapper.find(".atrNameInp").val();
    data["active"] = attributeWrapper.find(".atrActiveChbx").is(":checked") ? 1 : 0;
    let valueArr = [];
    attributeWrapper.find(".atrValueSpan").each(function () {
        valueArr.push($(this).text().replace(/\r?\n|\r/g, " ").trim());
    })
    data["value"] = valueArr;

    if ($("#exampleModalCenter2").hasClass("show")) {
        updateAttribute(keyname, data, attributesObject);

    } else {
        //case add new attribute -> check if existByName
        if (existByName(data["name"], attributesObject)) {
            alert(keyname + " is exist!");
        } else {
            attributesObject[keyname] = data;
        }
    }


    let curModal = saveFormBtn.closest(".modal");

    $(curModal).modal('hide');
    renderDataForAttributeTable(attributesObject);


}

//add image as paste way by a ordering
window.addEventListener("paste", (e) => {
    if (e.clipboardData.files.length > 0) {
        let fileinputs = []
        let curinp;
        fileinputs = document.querySelectorAll(".file_inp");
        fileinputs.forEach(function (inp) {

            if ($(inp).parent().find('img').attr("src") === "/resource/img/default.png") {
                inp.files = e.clipboardData.files;
                let fileReader = new FileReader();

                fileReader.readAsDataURL(e.clipboardData.files[0]);
                fileReader.onload = () => {
                    $(inp).parent().find('img').attr("src", fileReader.result);

                }
                return;
            }


        })


    }
});


















