rd /s /q %~dp0apk-out
md %~dp0apk-out

for /d %%i in (%~dp0*WuYe) do (
call cd %%i
call ant clean
call ant release
for %%k in (%%i\bin\*-release.apk) do (
copy %%k %~dp0apk-out\
)
call ant clean
)
pause