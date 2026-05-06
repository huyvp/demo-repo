const initApp = () => {
    const button = document.querySelector('button');
    button.addEventListener('click', debounce(clickOrder, 2000))

    const input = document.querySelector('input');
    const defaultText = document.getElementById('default');
    const debounceText = document.getElementById('debounce');
    const throttleText = document.getElementById('throttle');

    const updateDebounceText = debounce(text => {
        debounceText.textContent = text;
    });
    const updateThrottleText = throttle(text => {
        throttleText.textContent = text;
    })

    input.addEventListener('input', e => {
        defaultText.textContent = e.target.value;
        updateDebounceText(e.target.value);
        updateThrottleText(e.target.value)
    })
}
const clickOrder = () => {
    console.log('Order');
}

document.addEventListener('DOMContentLoaded', initApp);

function debounce(func, delay = 1000) {
    let timeout;
    return (...args) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => {
            func(...args)
        }, delay)
    }
}

function throttle(func, delay = 1000) {
    let shouldWait = false;
    let waitingArgs;

    const timeoutFunc = () => {
        if (waitingArgs === null) {
            shouldWait = false
        }
        else {
            func(...waitingArgs);
            waitingArgs = null;
            setTimeout(timeoutFunc, delay);
        }
    }

    return (...args) => {
        if (shouldWait) {
            waitingArgs = args;
            return;
        };
        func(...args);
        shouldWait = true;

        setTimeout(timeoutFunc, delay)
    }
}