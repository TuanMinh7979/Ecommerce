var attributesObject = {};

var numOfImage = 1;
var MAX_FILE_SIZE = 512000;

callAttributeApi(`/admin/product/api/${productId}/attributes`);

$(function () {
    callCategoriesData();

    $("#shorDescription").richText();
    $("#fullDescription").richText();


})

$("#selectnone-btn").on("click",
    function (event) {
        event.preventDefault();
        handleSelectDefaultBtn(this, "delImageIds", defaultImage);
    })

//Color picker
$(document).on("click", ".colorpicker__span", function () {
    $(this).parent().parent().find("input[type='checkbox']").prop('checked', true);
    $("#launch-cp-modal").modal("show");

})
$(".color-item").on("click", function () {
    let colorText = $(this).text();
    let inpCb = $(document).find(".chb-item input[type='checkbox']:checked");

    $(inpCb).parent().parent().find(".color-text").val(colorText);
    $("#launch-cp-modal").modal("hide");
});

$("#launch-cp-modal").on("hidden.bs.modal", function () {
    let curCb = $(document).find(".chb-item input[type='checkbox']:checked")

    $(curCb).prop("checked", false)
});
///Color picker


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

    data.map(function (cat) {
        if (cat.id != productCategoryId) {
            opts += `<option value="${cat.id}">${cat.name}</option> `

        } else {
            opts += `<option value="${cat.id}" selected>${cat.name}</option> `
        }
    })


    $("#category-sel").append(opts);

}


//DETAIL ATTRIBUTE TAB


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
    $(checkboxes).each(function () {
        let nameToDel = $(this).parent().parent().find("td:nth-child(2)").text();
        delete attributesObject[nameToDel];
        $(this).closest("tr").remove();

    })
});


$(document).on("click", '.tag_delete_one', function (event) {
    event.preventDefault();
    let nameToDel = $(this).parent().parent().find("td:nth-child(2)").text();
    delete attributesObject[nameToDel];
    $(this).closest("tr").remove();
})


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

function renderDataForAttributeTable(atbs) {
    let rs = "";

    // alert(Object.keys(attributesObject).length)
    if (Object.keys(atbs).length != 0) {
        for (let atbi in atbs) {
            let curAtr = atbs[atbi];

            rs += `       <tr class="col-12">
                <td class="col-1"><input type="checkbox" class="atb-iddel-checkbox" value="${atbi}"></td>
                <td class="col-5" class="atb-name-inp" ">${atbi}</td>
                <td class="col-3"><i class="atb-active-checkbox fas fa-circle" isactive="${curAtr.active}"></i>  </td>
                  <td class="col-3">
                    <button class="editAttributeBtn btn btn-default" >Edit</button>
                    <a class="btn btn-danger tag_delete_one"  >Delete</a>

                </td>

            </tr>`;
        }
        $("#tabledata").html(rs);
        setActiveCheckbox();
    } else {
        rs += "<div class=\"alert alert-warning\" role=\"alert\">\n" +
            "There are not any attribute!" +
            '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
            '           <span aria-hidden="true">&times;</span>' +
            "        </button>" +
            "</div>"
        $("#attribute-table").before(rs);
    }


}

function callAttributeApi(url) {

    $.ajax({
        type: "get",
        url: url,
        success: function (data) {
            if (data !== null && data !== undefined) {
                attributesObject = JSON.parse(data);
                renderDataForAttributeTable(attributesObject);
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

$('#mainForm').submit(function () {
    let atbString = JSON.stringify(attributesObject);
    // console.log(atbString);
    $(this).append(`<input type="hidden" name="atbs" />`);

    let allHiddenInp = $(this).find('input[type="hidden"]');
    $(allHiddenInp).each(function () {
        if ($(this).attr('name') == "atbs") {
            $(this).val(atbString);
        }
    })
    return true;
});


//add image as paste way by a ordering
window.addEventListener("paste", (e) => {
    if (e.clipboardData.files.length > 0) {
        let fileinputs = []

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


//CALL CATEGORIES FOR FORM


$(document).on("change", ".file_inp", function (event) {
    event.preventDefault();
    changeImage(this.files[0], this, "delImageIds", defaultImage);
    //remove id div if is old image in system.(dont update it -> add it)
    let imageWrapperDiv = $(this).parent();
    let idImageWrapperDiv = $(imageWrapperDiv).prop('id');
    if (idImageWrapperDiv != undefined) {
        $(imageWrapperDiv).prop('id', "-1");
    }

});


function createNewEmptyExtraImage() {


    event.preventDefault();
    let html = `<div class="img-wrapper col-6">
         <div class="row img-wrapper-header">
                    <span class="col-4 img-wrapper-header-name">Extra</span>

                    <div class="col-8 img-wrapper-header-opts row " style=" display:flex; justify-content: flex-end">
                        <div class="opts-item colorpicker">
                            <input name="mainColor" type="text" class="color-text" value="no color"/>
                            <span class="colorpicker__span"><i class="colorpicker__i fas fa-eye-dropper"></i></span>
                        </div>
                        <div class="opts-item close-i"><i class="fas fa-trash"></i></div>
                        <div class="opts-item chb-item" style="display:none"><input type="checkbox"></div>
                    </div>
                </div>
        <div class="image-preview new-image-preview">
            <img src="${defaultImage}" alt="alt" class="image-preview__img"/>
        </div>
         <input type="file" class="file_inp"/>

    </div>`

    $("#imageWrapper").append(html);


}


$(document).on("click", "#addNewImageExtraBtn", createNewEmptyExtraImage);


$(document).on("click", ".close-i", function () {
    // alert(productId);
    let imgWrapperDiv = $(this).parent().parent().parent();
    let curId = $(imgWrapperDiv).prop('id');
    if (curId != undefined && curId != "-1") {
        let oldIds = document.getElementById("delImageIds").value.trim();
        document.getElementById("delImageIds").value = oldIds + " " + this.parentElement.parentElement.parentElement.id + " ";
        console.log("close-i nhe" + document.getElementById("delImageIds").value);
        // alert(document.getElementById("delImageIds").value);
    }
    $(imgWrapperDiv).remove();
})

$("#saveChangesBtn").on("click", function () {
    let delIds = document.getElementById("delImageIds").value;
    let param = {"delImageIds": delIds};
    ;
    if (delIds != "") {
        deleteImgs(param);
    } else {
        addUpdateTheRest();
    }

})

function deleteImgs(param) {


    $.ajax({
        url: `/admin/product/edit/${productId}/manage-image/delete`,
        type: "Post",
        data: param,


        success(data) {
            console.log("XOA THANH CONG");
            addUpdateTheRest();

        },
        error: function (request, status, error) {

        }

    });

}

function addUpdateTheRest() {
    let allImageWrapper = $(".img-wrapper");
    let url = "";
    $(allImageWrapper).each(function () {
        var formData = new FormData();
        let fileInp = $(this).find(".file_inp");

        alert($(this).find(".image-preview__img").prop('src'))
        alert(defaultImage);


        if ($(this).find(".image-preview__img").prop('src').includes(defaultImage)) {
            return;
        }


        if ($(fileInp)[0].files[0] != undefined) {
            if (($(this).attr('id') == undefined || $(this).attr('id') == "-1")) {
                formData.append("file", $(fileInp)[0].files[0]);
                console.log($(fileInp)[0].files[0]);
                url = `/admin/product/edit/${productId}/manage-image/add`;
                console.log("ADd NHA " + $(this).attr('id'))
            }
        } else {
            console.log("UPDATE NHA " + $(this).attr('id'))
            let curId = $(this).attr('id');
            url = `/admin/product/edit/${productId}/manage-image/update/${curId}`;
        }
        formData.append("color", $(this).find(".color-text").val());
        if ($(this).hasClass("main-img")) {
            formData.append("isMain", "true");
        }

        $.ajax({
            url: url,
            enctype: 'multipart/form-data',
            type: "Post",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,

            success(data) {
                console.log("scucesss...")

            },
            error: function (request, status, error) {
                console.log("errorr occur")
            }


        })
    })
}

function addOrUpdate(url, formdiv) {


}

$("#delManyBtn").on("click", function () {
    $(".close-i").css("display", $(".close-i").css("display") == "none" ? "block" : "none");
    $(".chb-item").css("display", $(".chb-item").css("display") == "block" ? "none" : "block");

})























