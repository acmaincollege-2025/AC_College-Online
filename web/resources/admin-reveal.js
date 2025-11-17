 document.addEventListener('keydown', function (e) {
        if (e.ctrlKey && e.shiftKey && e.key === 'L') {
            let link = document.getElementById('adminLink');
            if (link) {
                link.style.display = 'inline';
            }
        }
    });