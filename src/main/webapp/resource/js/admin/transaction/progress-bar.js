const previousBtn = document.getElementById('previousBtn');
const nextBtn = document.getElementById('nextBtn');
const finishBtn = document.getElementById('finishBtn');
const content = document.getElementById('content');
const bullets = [...document.querySelectorAll('.bullet')];

const MAX_STEPS = 4;
var currentStep = "";

nextBtn.addEventListener('click', (event) => {

    event.preventDefault()
    bullets[currentStep ].classList.add('completed');
    let statusInp = document.getElementById("tStatusInp");
    statusInp.value = bullets[currentStep ].previousSibling.previousSibling.innerHTML;
    currentStep += 1;

    previousBtn.disabled = false;
    if (currentStep === MAX_STEPS) {
        nextBtn.disabled = true;
    }

});


previousBtn.addEventListener('click', (event) => {

    event.preventDefault()
    bullets[currentStep - 1].classList.remove('completed');
    let statusInp = document.getElementById("tStatusInp");
    statusInp.value = bullets[currentStep -2].previousSibling.previousSibling.innerHTML;
    currentStep -= 1;
    nextBtn.disabled = false;

    if (currentStep === 1) {
        previousBtn.disabled = true;
    }

});

