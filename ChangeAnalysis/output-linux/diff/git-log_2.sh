#!/bin/bash
mkdir /root/git/linuxkernel/diff
cd /root/git/linuxkernel/linuxkernel
git log --pretty=fuller -p --stat v2.6.23.10 v2.6.23.11 > /root/git/linuxkernel/diff/307-308.txt
echo writing to: /root/git/linuxkernel/diff/307-308.txt
git log --pretty=fuller -p --stat v2.6.23.11 v2.6.23.12 > /root/git/linuxkernel/diff/308-309.txt
echo writing to: /root/git/linuxkernel/diff/308-309.txt
git log --pretty=fuller -p --stat v2.6.23.12 v2.6.23.13 > /root/git/linuxkernel/diff/309-310.txt
echo writing to: /root/git/linuxkernel/diff/309-310.txt
git log --pretty=fuller -p --stat v2.6.23.13 v2.6.23.14 > /root/git/linuxkernel/diff/310-311.txt
echo writing to: /root/git/linuxkernel/diff/310-311.txt
git log --pretty=fuller -p --stat v2.6.23.14 v2.6.23.15 > /root/git/linuxkernel/diff/311-312.txt
echo writing to: /root/git/linuxkernel/diff/311-312.txt
git log --pretty=fuller -p --stat v2.6.23.15 v2.6.23.16 > /root/git/linuxkernel/diff/312-313.txt
echo writing to: /root/git/linuxkernel/diff/312-313.txt
git log --pretty=fuller -p --stat v2.6.23.16 v2.6.23.17 > /root/git/linuxkernel/diff/313-314.txt
echo writing to: /root/git/linuxkernel/diff/313-314.txt
git log --pretty=fuller -p --stat v2.6.23.17 v2.6.24 > /root/git/linuxkernel/diff/314-315.txt
echo writing to: /root/git/linuxkernel/diff/314-315.txt
git log --pretty=fuller -p --stat v2.6.24 v2.6.24-rc1 > /root/git/linuxkernel/diff/315-316.txt
echo writing to: /root/git/linuxkernel/diff/315-316.txt
git log --pretty=fuller -p --stat v2.6.24-rc1 v2.6.24-rc2 > /root/git/linuxkernel/diff/316-317.txt
echo writing to: /root/git/linuxkernel/diff/316-317.txt
git log --pretty=fuller -p --stat v2.6.24-rc2 v2.6.24-rc3 > /root/git/linuxkernel/diff/317-318.txt
echo writing to: /root/git/linuxkernel/diff/317-318.txt
git log --pretty=fuller -p --stat v2.6.24-rc3 v2.6.24-rc4 > /root/git/linuxkernel/diff/318-319.txt
echo writing to: /root/git/linuxkernel/diff/318-319.txt
git log --pretty=fuller -p --stat v2.6.24-rc4 v2.6.24-rc5 > /root/git/linuxkernel/diff/319-320.txt
echo writing to: /root/git/linuxkernel/diff/319-320.txt
git log --pretty=fuller -p --stat v2.6.24-rc5 v2.6.24-rc6 > /root/git/linuxkernel/diff/320-321.txt
echo writing to: /root/git/linuxkernel/diff/320-321.txt
git log --pretty=fuller -p --stat v2.6.24-rc6 v2.6.24-rc7 > /root/git/linuxkernel/diff/321-322.txt
echo writing to: /root/git/linuxkernel/diff/321-322.txt
git log --pretty=fuller -p --stat v2.6.24-rc7 v2.6.24-rc8 > /root/git/linuxkernel/diff/322-323.txt
echo writing to: /root/git/linuxkernel/diff/322-323.txt
git log --pretty=fuller -p --stat v2.6.24-rc8 v2.6.24.1 > /root/git/linuxkernel/diff/323-324.txt
echo writing to: /root/git/linuxkernel/diff/323-324.txt
git log --pretty=fuller -p --stat v2.6.24.1 v2.6.24.2 > /root/git/linuxkernel/diff/324-325.txt
echo writing to: /root/git/linuxkernel/diff/324-325.txt
git log --pretty=fuller -p --stat v2.6.24.2 v2.6.24.3 > /root/git/linuxkernel/diff/325-326.txt
echo writing to: /root/git/linuxkernel/diff/325-326.txt
git log --pretty=fuller -p --stat v2.6.24.3 v2.6.24.4 > /root/git/linuxkernel/diff/326-327.txt
echo writing to: /root/git/linuxkernel/diff/326-327.txt
git log --pretty=fuller -p --stat v2.6.24.4 v2.6.24.5 > /root/git/linuxkernel/diff/327-328.txt
echo writing to: /root/git/linuxkernel/diff/327-328.txt
git log --pretty=fuller -p --stat v2.6.24.5 v2.6.24.6 > /root/git/linuxkernel/diff/328-329.txt
echo writing to: /root/git/linuxkernel/diff/328-329.txt
git log --pretty=fuller -p --stat v2.6.24.6 v2.6.24.7 > /root/git/linuxkernel/diff/329-330.txt
echo writing to: /root/git/linuxkernel/diff/329-330.txt
git log --pretty=fuller -p --stat v2.6.24.7 v2.6.25 > /root/git/linuxkernel/diff/330-331.txt
echo writing to: /root/git/linuxkernel/diff/330-331.txt
git log --pretty=fuller -p --stat v2.6.25 v2.6.25-rc1 > /root/git/linuxkernel/diff/331-332.txt
echo writing to: /root/git/linuxkernel/diff/331-332.txt
git log --pretty=fuller -p --stat v2.6.25-rc1 v2.6.25-rc2 > /root/git/linuxkernel/diff/332-333.txt
echo writing to: /root/git/linuxkernel/diff/332-333.txt
git log --pretty=fuller -p --stat v2.6.25-rc2 v2.6.25-rc3 > /root/git/linuxkernel/diff/333-334.txt
echo writing to: /root/git/linuxkernel/diff/333-334.txt
git log --pretty=fuller -p --stat v2.6.25-rc3 v2.6.25-rc4 > /root/git/linuxkernel/diff/334-335.txt
echo writing to: /root/git/linuxkernel/diff/334-335.txt
git log --pretty=fuller -p --stat v2.6.25-rc4 v2.6.25-rc5 > /root/git/linuxkernel/diff/335-336.txt
echo writing to: /root/git/linuxkernel/diff/335-336.txt
git log --pretty=fuller -p --stat v2.6.25-rc5 v2.6.25-rc6 > /root/git/linuxkernel/diff/336-337.txt
echo writing to: /root/git/linuxkernel/diff/336-337.txt
git log --pretty=fuller -p --stat v2.6.25-rc6 v2.6.25-rc7 > /root/git/linuxkernel/diff/337-338.txt
echo writing to: /root/git/linuxkernel/diff/337-338.txt
git log --pretty=fuller -p --stat v2.6.25-rc7 v2.6.25-rc8 > /root/git/linuxkernel/diff/338-339.txt
echo writing to: /root/git/linuxkernel/diff/338-339.txt
git log --pretty=fuller -p --stat v2.6.25-rc8 v2.6.25-rc9 > /root/git/linuxkernel/diff/339-340.txt
echo writing to: /root/git/linuxkernel/diff/339-340.txt
git log --pretty=fuller -p --stat v2.6.25-rc9 v2.6.25.1 > /root/git/linuxkernel/diff/340-341.txt
echo writing to: /root/git/linuxkernel/diff/340-341.txt
git log --pretty=fuller -p --stat v2.6.25.1 v2.6.25.2 > /root/git/linuxkernel/diff/341-342.txt
echo writing to: /root/git/linuxkernel/diff/341-342.txt
git log --pretty=fuller -p --stat v2.6.25.2 v2.6.25.3 > /root/git/linuxkernel/diff/342-343.txt
echo writing to: /root/git/linuxkernel/diff/342-343.txt
git log --pretty=fuller -p --stat v2.6.25.3 v2.6.25.4 > /root/git/linuxkernel/diff/343-344.txt
echo writing to: /root/git/linuxkernel/diff/343-344.txt
git log --pretty=fuller -p --stat v2.6.25.4 v2.6.25.5 > /root/git/linuxkernel/diff/344-345.txt
echo writing to: /root/git/linuxkernel/diff/344-345.txt
git log --pretty=fuller -p --stat v2.6.25.5 v2.6.25.6 > /root/git/linuxkernel/diff/345-346.txt
echo writing to: /root/git/linuxkernel/diff/345-346.txt
git log --pretty=fuller -p --stat v2.6.25.6 v2.6.25.7 > /root/git/linuxkernel/diff/346-347.txt
echo writing to: /root/git/linuxkernel/diff/346-347.txt
git log --pretty=fuller -p --stat v2.6.25.7 v2.6.25.8 > /root/git/linuxkernel/diff/347-348.txt
echo writing to: /root/git/linuxkernel/diff/347-348.txt
git log --pretty=fuller -p --stat v2.6.25.8 v2.6.25.9 > /root/git/linuxkernel/diff/348-349.txt
echo writing to: /root/git/linuxkernel/diff/348-349.txt
git log --pretty=fuller -p --stat v2.6.25.9 v2.6.25.10 > /root/git/linuxkernel/diff/349-350.txt
echo writing to: /root/git/linuxkernel/diff/349-350.txt
git log --pretty=fuller -p --stat v2.6.25.10 v2.6.25.11 > /root/git/linuxkernel/diff/350-351.txt
echo writing to: /root/git/linuxkernel/diff/350-351.txt
git log --pretty=fuller -p --stat v2.6.25.11 v2.6.25.12 > /root/git/linuxkernel/diff/351-352.txt
echo writing to: /root/git/linuxkernel/diff/351-352.txt
git log --pretty=fuller -p --stat v2.6.25.12 v2.6.25.13 > /root/git/linuxkernel/diff/352-353.txt
echo writing to: /root/git/linuxkernel/diff/352-353.txt
git log --pretty=fuller -p --stat v2.6.25.13 v2.6.25.14 > /root/git/linuxkernel/diff/353-354.txt
echo writing to: /root/git/linuxkernel/diff/353-354.txt
git log --pretty=fuller -p --stat v2.6.25.14 v2.6.25.15 > /root/git/linuxkernel/diff/354-355.txt
echo writing to: /root/git/linuxkernel/diff/354-355.txt
git log --pretty=fuller -p --stat v2.6.25.15 v2.6.25.16 > /root/git/linuxkernel/diff/355-356.txt
echo writing to: /root/git/linuxkernel/diff/355-356.txt
git log --pretty=fuller -p --stat v2.6.25.16 v2.6.25.17 > /root/git/linuxkernel/diff/356-357.txt
echo writing to: /root/git/linuxkernel/diff/356-357.txt
git log --pretty=fuller -p --stat v2.6.25.17 v2.6.25.18 > /root/git/linuxkernel/diff/357-358.txt
echo writing to: /root/git/linuxkernel/diff/357-358.txt
git log --pretty=fuller -p --stat v2.6.25.18 v2.6.25.19 > /root/git/linuxkernel/diff/358-359.txt
echo writing to: /root/git/linuxkernel/diff/358-359.txt
git log --pretty=fuller -p --stat v2.6.25.19 v2.6.25.20 > /root/git/linuxkernel/diff/359-360.txt
echo writing to: /root/git/linuxkernel/diff/359-360.txt
git log --pretty=fuller -p --stat v2.6.25.20 v2.6.26 > /root/git/linuxkernel/diff/360-361.txt
echo writing to: /root/git/linuxkernel/diff/360-361.txt
git log --pretty=fuller -p --stat v2.6.26 v2.6.26-rc1 > /root/git/linuxkernel/diff/361-362.txt
echo writing to: /root/git/linuxkernel/diff/361-362.txt
git log --pretty=fuller -p --stat v2.6.26-rc1 v2.6.26-rc2 > /root/git/linuxkernel/diff/362-363.txt
echo writing to: /root/git/linuxkernel/diff/362-363.txt
git log --pretty=fuller -p --stat v2.6.26-rc2 v2.6.26-rc3 > /root/git/linuxkernel/diff/363-364.txt
echo writing to: /root/git/linuxkernel/diff/363-364.txt
git log --pretty=fuller -p --stat v2.6.26-rc3 v2.6.26-rc4 > /root/git/linuxkernel/diff/364-365.txt
echo writing to: /root/git/linuxkernel/diff/364-365.txt
git log --pretty=fuller -p --stat v2.6.26-rc4 v2.6.26-rc5 > /root/git/linuxkernel/diff/365-366.txt
echo writing to: /root/git/linuxkernel/diff/365-366.txt
git log --pretty=fuller -p --stat v2.6.26-rc5 v2.6.26-rc6 > /root/git/linuxkernel/diff/366-367.txt
echo writing to: /root/git/linuxkernel/diff/366-367.txt
git log --pretty=fuller -p --stat v2.6.26-rc6 v2.6.26-rc7 > /root/git/linuxkernel/diff/367-368.txt
echo writing to: /root/git/linuxkernel/diff/367-368.txt
git log --pretty=fuller -p --stat v2.6.26-rc7 v2.6.26-rc8 > /root/git/linuxkernel/diff/368-369.txt
echo writing to: /root/git/linuxkernel/diff/368-369.txt
git log --pretty=fuller -p --stat v2.6.26-rc8 v2.6.26-rc9 > /root/git/linuxkernel/diff/369-370.txt
echo writing to: /root/git/linuxkernel/diff/369-370.txt
git log --pretty=fuller -p --stat v2.6.26-rc9 v2.6.26.1 > /root/git/linuxkernel/diff/370-371.txt
echo writing to: /root/git/linuxkernel/diff/370-371.txt
git log --pretty=fuller -p --stat v2.6.26.1 v2.6.26.2 > /root/git/linuxkernel/diff/371-372.txt
echo writing to: /root/git/linuxkernel/diff/371-372.txt
git log --pretty=fuller -p --stat v2.6.26.2 v2.6.26.3 > /root/git/linuxkernel/diff/372-373.txt
echo writing to: /root/git/linuxkernel/diff/372-373.txt
git log --pretty=fuller -p --stat v2.6.26.3 v2.6.26.4 > /root/git/linuxkernel/diff/373-374.txt
echo writing to: /root/git/linuxkernel/diff/373-374.txt
git log --pretty=fuller -p --stat v2.6.26.4 v2.6.26.5 > /root/git/linuxkernel/diff/374-375.txt
echo writing to: /root/git/linuxkernel/diff/374-375.txt
git log --pretty=fuller -p --stat v2.6.26.5 v2.6.26.6 > /root/git/linuxkernel/diff/375-376.txt
echo writing to: /root/git/linuxkernel/diff/375-376.txt
git log --pretty=fuller -p --stat v2.6.26.6 v2.6.26.7 > /root/git/linuxkernel/diff/376-377.txt
echo writing to: /root/git/linuxkernel/diff/376-377.txt
git log --pretty=fuller -p --stat v2.6.26.7 v2.6.26.8 > /root/git/linuxkernel/diff/377-378.txt
echo writing to: /root/git/linuxkernel/diff/377-378.txt
git log --pretty=fuller -p --stat v2.6.26.8 v2.6.27 > /root/git/linuxkernel/diff/378-379.txt
echo writing to: /root/git/linuxkernel/diff/378-379.txt
git log --pretty=fuller -p --stat v2.6.27 v2.6.27-rc1 > /root/git/linuxkernel/diff/379-380.txt
echo writing to: /root/git/linuxkernel/diff/379-380.txt
git log --pretty=fuller -p --stat v2.6.27-rc1 v2.6.27-rc2 > /root/git/linuxkernel/diff/380-381.txt
echo writing to: /root/git/linuxkernel/diff/380-381.txt
git log --pretty=fuller -p --stat v2.6.27-rc2 v2.6.27-rc3 > /root/git/linuxkernel/diff/381-382.txt
echo writing to: /root/git/linuxkernel/diff/381-382.txt
git log --pretty=fuller -p --stat v2.6.27-rc3 v2.6.27-rc4 > /root/git/linuxkernel/diff/382-383.txt
echo writing to: /root/git/linuxkernel/diff/382-383.txt
git log --pretty=fuller -p --stat v2.6.27-rc4 v2.6.27-rc5 > /root/git/linuxkernel/diff/383-384.txt
echo writing to: /root/git/linuxkernel/diff/383-384.txt
git log --pretty=fuller -p --stat v2.6.27-rc5 v2.6.27-rc6 > /root/git/linuxkernel/diff/384-385.txt
echo writing to: /root/git/linuxkernel/diff/384-385.txt
git log --pretty=fuller -p --stat v2.6.27-rc6 v2.6.27-rc7 > /root/git/linuxkernel/diff/385-386.txt
echo writing to: /root/git/linuxkernel/diff/385-386.txt
git log --pretty=fuller -p --stat v2.6.27-rc7 v2.6.27-rc8 > /root/git/linuxkernel/diff/386-387.txt
echo writing to: /root/git/linuxkernel/diff/386-387.txt
git log --pretty=fuller -p --stat v2.6.27-rc8 v2.6.27-rc9 > /root/git/linuxkernel/diff/387-388.txt
echo writing to: /root/git/linuxkernel/diff/387-388.txt
git log --pretty=fuller -p --stat v2.6.27-rc9 v2.6.27.1 > /root/git/linuxkernel/diff/388-389.txt
echo writing to: /root/git/linuxkernel/diff/388-389.txt
git log --pretty=fuller -p --stat v2.6.27.1 v2.6.27.2 > /root/git/linuxkernel/diff/389-390.txt
echo writing to: /root/git/linuxkernel/diff/389-390.txt
git log --pretty=fuller -p --stat v2.6.27.2 v2.6.27.3 > /root/git/linuxkernel/diff/390-391.txt
echo writing to: /root/git/linuxkernel/diff/390-391.txt
git log --pretty=fuller -p --stat v2.6.27.3 v2.6.27.4 > /root/git/linuxkernel/diff/391-392.txt
echo writing to: /root/git/linuxkernel/diff/391-392.txt
git log --pretty=fuller -p --stat v2.6.27.4 v2.6.27.5 > /root/git/linuxkernel/diff/392-393.txt
echo writing to: /root/git/linuxkernel/diff/392-393.txt
git log --pretty=fuller -p --stat v2.6.27.5 v2.6.27.6 > /root/git/linuxkernel/diff/393-394.txt
echo writing to: /root/git/linuxkernel/diff/393-394.txt
git log --pretty=fuller -p --stat v2.6.27.6 v2.6.27.7 > /root/git/linuxkernel/diff/394-395.txt
echo writing to: /root/git/linuxkernel/diff/394-395.txt
git log --pretty=fuller -p --stat v2.6.27.7 v2.6.27.8 > /root/git/linuxkernel/diff/395-396.txt
echo writing to: /root/git/linuxkernel/diff/395-396.txt
git log --pretty=fuller -p --stat v2.6.27.8 v2.6.27.9 > /root/git/linuxkernel/diff/396-397.txt
echo writing to: /root/git/linuxkernel/diff/396-397.txt
git log --pretty=fuller -p --stat v2.6.27.9 v2.6.27.10 > /root/git/linuxkernel/diff/397-398.txt
echo writing to: /root/git/linuxkernel/diff/397-398.txt
git log --pretty=fuller -p --stat v2.6.27.10 v2.6.27.11 > /root/git/linuxkernel/diff/398-399.txt
echo writing to: /root/git/linuxkernel/diff/398-399.txt
git log --pretty=fuller -p --stat v2.6.27.11 v2.6.27.12 > /root/git/linuxkernel/diff/399-400.txt
echo writing to: /root/git/linuxkernel/diff/399-400.txt
git log --pretty=fuller -p --stat v2.6.27.12 v2.6.27.13 > /root/git/linuxkernel/diff/400-401.txt
echo writing to: /root/git/linuxkernel/diff/400-401.txt
git log --pretty=fuller -p --stat v2.6.27.13 v2.6.27.14 > /root/git/linuxkernel/diff/401-402.txt
echo writing to: /root/git/linuxkernel/diff/401-402.txt
git log --pretty=fuller -p --stat v2.6.27.14 v2.6.27.15 > /root/git/linuxkernel/diff/402-403.txt
echo writing to: /root/git/linuxkernel/diff/402-403.txt
git log --pretty=fuller -p --stat v2.6.27.15 v2.6.27.16 > /root/git/linuxkernel/diff/403-404.txt
echo writing to: /root/git/linuxkernel/diff/403-404.txt
git log --pretty=fuller -p --stat v2.6.27.16 v2.6.27.17 > /root/git/linuxkernel/diff/404-405.txt
echo writing to: /root/git/linuxkernel/diff/404-405.txt
git log --pretty=fuller -p --stat v2.6.27.17 v2.6.27.18 > /root/git/linuxkernel/diff/405-406.txt
echo writing to: /root/git/linuxkernel/diff/405-406.txt
git log --pretty=fuller -p --stat v2.6.27.18 v2.6.27.19 > /root/git/linuxkernel/diff/406-407.txt
echo writing to: /root/git/linuxkernel/diff/406-407.txt
git log --pretty=fuller -p --stat v2.6.27.19 v2.6.27.20 > /root/git/linuxkernel/diff/407-408.txt
echo writing to: /root/git/linuxkernel/diff/407-408.txt
git log --pretty=fuller -p --stat v2.6.27.20 v2.6.27.21 > /root/git/linuxkernel/diff/408-409.txt
echo writing to: /root/git/linuxkernel/diff/408-409.txt
git log --pretty=fuller -p --stat v2.6.27.21 v2.6.27.22 > /root/git/linuxkernel/diff/409-410.txt
echo writing to: /root/git/linuxkernel/diff/409-410.txt
git log --pretty=fuller -p --stat v2.6.27.22 v2.6.27.23 > /root/git/linuxkernel/diff/410-411.txt
echo writing to: /root/git/linuxkernel/diff/410-411.txt
git log --pretty=fuller -p --stat v2.6.27.23 v2.6.27.24 > /root/git/linuxkernel/diff/411-412.txt
echo writing to: /root/git/linuxkernel/diff/411-412.txt
git log --pretty=fuller -p --stat v2.6.27.24 v2.6.27.25 > /root/git/linuxkernel/diff/412-413.txt
echo writing to: /root/git/linuxkernel/diff/412-413.txt
git log --pretty=fuller -p --stat v2.6.27.25 v2.6.27.26 > /root/git/linuxkernel/diff/413-414.txt
echo writing to: /root/git/linuxkernel/diff/413-414.txt
git log --pretty=fuller -p --stat v2.6.27.26 v2.6.27.27 > /root/git/linuxkernel/diff/414-415.txt
echo writing to: /root/git/linuxkernel/diff/414-415.txt
git log --pretty=fuller -p --stat v2.6.27.27 v2.6.27.28 > /root/git/linuxkernel/diff/415-416.txt
echo writing to: /root/git/linuxkernel/diff/415-416.txt
git log --pretty=fuller -p --stat v2.6.27.28 v2.6.27.29 > /root/git/linuxkernel/diff/416-417.txt
echo writing to: /root/git/linuxkernel/diff/416-417.txt
git log --pretty=fuller -p --stat v2.6.27.29 v2.6.27.30 > /root/git/linuxkernel/diff/417-418.txt
echo writing to: /root/git/linuxkernel/diff/417-418.txt
git log --pretty=fuller -p --stat v2.6.27.30 v2.6.27.31 > /root/git/linuxkernel/diff/418-419.txt
echo writing to: /root/git/linuxkernel/diff/418-419.txt
git log --pretty=fuller -p --stat v2.6.27.31 v2.6.27.32 > /root/git/linuxkernel/diff/419-420.txt
echo writing to: /root/git/linuxkernel/diff/419-420.txt
git log --pretty=fuller -p --stat v2.6.27.32 v2.6.27.33 > /root/git/linuxkernel/diff/420-421.txt
echo writing to: /root/git/linuxkernel/diff/420-421.txt
git log --pretty=fuller -p --stat v2.6.27.33 v2.6.27.34 > /root/git/linuxkernel/diff/421-422.txt
echo writing to: /root/git/linuxkernel/diff/421-422.txt
git log --pretty=fuller -p --stat v2.6.27.34 v2.6.27.35 > /root/git/linuxkernel/diff/422-423.txt
echo writing to: /root/git/linuxkernel/diff/422-423.txt
git log --pretty=fuller -p --stat v2.6.27.35 v2.6.27.36 > /root/git/linuxkernel/diff/423-424.txt
echo writing to: /root/git/linuxkernel/diff/423-424.txt
git log --pretty=fuller -p --stat v2.6.27.36 v2.6.27.37 > /root/git/linuxkernel/diff/424-425.txt
echo writing to: /root/git/linuxkernel/diff/424-425.txt
git log --pretty=fuller -p --stat v2.6.27.37 v2.6.27.38 > /root/git/linuxkernel/diff/425-426.txt
echo writing to: /root/git/linuxkernel/diff/425-426.txt
git log --pretty=fuller -p --stat v2.6.27.38 v2.6.27.39 > /root/git/linuxkernel/diff/426-427.txt
echo writing to: /root/git/linuxkernel/diff/426-427.txt
git log --pretty=fuller -p --stat v2.6.27.39 v2.6.27.40 > /root/git/linuxkernel/diff/427-428.txt
echo writing to: /root/git/linuxkernel/diff/427-428.txt
git log --pretty=fuller -p --stat v2.6.27.40 v2.6.27.41 > /root/git/linuxkernel/diff/428-429.txt
echo writing to: /root/git/linuxkernel/diff/428-429.txt
git log --pretty=fuller -p --stat v2.6.27.41 v2.6.27.42 > /root/git/linuxkernel/diff/429-430.txt
echo writing to: /root/git/linuxkernel/diff/429-430.txt
git log --pretty=fuller -p --stat v2.6.27.42 v2.6.27.43 > /root/git/linuxkernel/diff/430-431.txt
echo writing to: /root/git/linuxkernel/diff/430-431.txt
git log --pretty=fuller -p --stat v2.6.27.43 v2.6.27.44 > /root/git/linuxkernel/diff/431-432.txt
echo writing to: /root/git/linuxkernel/diff/431-432.txt
git log --pretty=fuller -p --stat v2.6.27.44 v2.6.27.45 > /root/git/linuxkernel/diff/432-433.txt
echo writing to: /root/git/linuxkernel/diff/432-433.txt
git log --pretty=fuller -p --stat v2.6.27.45 v2.6.27.46 > /root/git/linuxkernel/diff/433-434.txt
echo writing to: /root/git/linuxkernel/diff/433-434.txt
git log --pretty=fuller -p --stat v2.6.27.46 v2.6.27.47 > /root/git/linuxkernel/diff/434-435.txt
echo writing to: /root/git/linuxkernel/diff/434-435.txt
git log --pretty=fuller -p --stat v2.6.27.47 v2.6.27.48 > /root/git/linuxkernel/diff/435-436.txt
echo writing to: /root/git/linuxkernel/diff/435-436.txt
git log --pretty=fuller -p --stat v2.6.27.48 v2.6.27.49 > /root/git/linuxkernel/diff/436-437.txt
echo writing to: /root/git/linuxkernel/diff/436-437.txt
git log --pretty=fuller -p --stat v2.6.27.49 v2.6.27.50 > /root/git/linuxkernel/diff/437-438.txt
echo writing to: /root/git/linuxkernel/diff/437-438.txt
git log --pretty=fuller -p --stat v2.6.27.50 v2.6.27.51 > /root/git/linuxkernel/diff/438-439.txt
echo writing to: /root/git/linuxkernel/diff/438-439.txt
git log --pretty=fuller -p --stat v2.6.27.51 v2.6.27.52 > /root/git/linuxkernel/diff/439-440.txt
echo writing to: /root/git/linuxkernel/diff/439-440.txt
git log --pretty=fuller -p --stat v2.6.27.52 v2.6.27.53 > /root/git/linuxkernel/diff/440-441.txt
echo writing to: /root/git/linuxkernel/diff/440-441.txt
git log --pretty=fuller -p --stat v2.6.27.53 v2.6.27.54 > /root/git/linuxkernel/diff/441-442.txt
echo writing to: /root/git/linuxkernel/diff/441-442.txt
git log --pretty=fuller -p --stat v2.6.27.54 v2.6.27.55 > /root/git/linuxkernel/diff/442-443.txt
echo writing to: /root/git/linuxkernel/diff/442-443.txt
git log --pretty=fuller -p --stat v2.6.27.55 v2.6.27.56 > /root/git/linuxkernel/diff/443-444.txt
echo writing to: /root/git/linuxkernel/diff/443-444.txt
git log --pretty=fuller -p --stat v2.6.27.56 v2.6.27.57 > /root/git/linuxkernel/diff/444-445.txt
echo writing to: /root/git/linuxkernel/diff/444-445.txt
git log --pretty=fuller -p --stat v2.6.27.57 v2.6.27.58 > /root/git/linuxkernel/diff/445-446.txt
echo writing to: /root/git/linuxkernel/diff/445-446.txt
git log --pretty=fuller -p --stat v2.6.27.58 v2.6.27.59 > /root/git/linuxkernel/diff/446-447.txt
echo writing to: /root/git/linuxkernel/diff/446-447.txt
git log --pretty=fuller -p --stat v2.6.27.59 v2.6.27.60 > /root/git/linuxkernel/diff/447-448.txt
echo writing to: /root/git/linuxkernel/diff/447-448.txt
git log --pretty=fuller -p --stat v2.6.27.60 v2.6.27.61 > /root/git/linuxkernel/diff/448-449.txt
echo writing to: /root/git/linuxkernel/diff/448-449.txt
git log --pretty=fuller -p --stat v2.6.27.61 v2.6.27.62 > /root/git/linuxkernel/diff/449-450.txt
echo writing to: /root/git/linuxkernel/diff/449-450.txt
git log --pretty=fuller -p --stat v2.6.27.62 v2.6.28 > /root/git/linuxkernel/diff/450-451.txt
echo writing to: /root/git/linuxkernel/diff/450-451.txt
git log --pretty=fuller -p --stat v2.6.28 v2.6.28-rc1 > /root/git/linuxkernel/diff/451-452.txt
echo writing to: /root/git/linuxkernel/diff/451-452.txt
git log --pretty=fuller -p --stat v2.6.28-rc1 v2.6.28-rc2 > /root/git/linuxkernel/diff/452-453.txt
echo writing to: /root/git/linuxkernel/diff/452-453.txt
git log --pretty=fuller -p --stat v2.6.28-rc2 v2.6.28-rc3 > /root/git/linuxkernel/diff/453-454.txt
echo writing to: /root/git/linuxkernel/diff/453-454.txt
git log --pretty=fuller -p --stat v2.6.28-rc3 v2.6.28-rc4 > /root/git/linuxkernel/diff/454-455.txt
echo writing to: /root/git/linuxkernel/diff/454-455.txt
git log --pretty=fuller -p --stat v2.6.28-rc4 v2.6.28-rc5 > /root/git/linuxkernel/diff/455-456.txt
echo writing to: /root/git/linuxkernel/diff/455-456.txt
git log --pretty=fuller -p --stat v2.6.28-rc5 v2.6.28-rc6 > /root/git/linuxkernel/diff/456-457.txt
echo writing to: /root/git/linuxkernel/diff/456-457.txt
git log --pretty=fuller -p --stat v2.6.28-rc6 v2.6.28-rc7 > /root/git/linuxkernel/diff/457-458.txt
echo writing to: /root/git/linuxkernel/diff/457-458.txt
git log --pretty=fuller -p --stat v2.6.28-rc7 v2.6.28-rc8 > /root/git/linuxkernel/diff/458-459.txt
echo writing to: /root/git/linuxkernel/diff/458-459.txt
git log --pretty=fuller -p --stat v2.6.28-rc8 v2.6.28-rc9 > /root/git/linuxkernel/diff/459-460.txt
echo writing to: /root/git/linuxkernel/diff/459-460.txt
git log --pretty=fuller -p --stat v2.6.28-rc9 v2.6.28.1 > /root/git/linuxkernel/diff/460-461.txt
echo writing to: /root/git/linuxkernel/diff/460-461.txt
git log --pretty=fuller -p --stat v2.6.28.1 v2.6.28.2 > /root/git/linuxkernel/diff/461-462.txt
echo writing to: /root/git/linuxkernel/diff/461-462.txt
git log --pretty=fuller -p --stat v2.6.28.2 v2.6.28.3 > /root/git/linuxkernel/diff/462-463.txt
echo writing to: /root/git/linuxkernel/diff/462-463.txt
git log --pretty=fuller -p --stat v2.6.28.3 v2.6.28.4 > /root/git/linuxkernel/diff/463-464.txt
echo writing to: /root/git/linuxkernel/diff/463-464.txt
git log --pretty=fuller -p --stat v2.6.28.4 v2.6.28.5 > /root/git/linuxkernel/diff/464-465.txt
echo writing to: /root/git/linuxkernel/diff/464-465.txt
git log --pretty=fuller -p --stat v2.6.28.5 v2.6.28.6 > /root/git/linuxkernel/diff/465-466.txt
echo writing to: /root/git/linuxkernel/diff/465-466.txt
git log --pretty=fuller -p --stat v2.6.28.6 v2.6.28.7 > /root/git/linuxkernel/diff/466-467.txt
echo writing to: /root/git/linuxkernel/diff/466-467.txt
git log --pretty=fuller -p --stat v2.6.28.7 v2.6.28.8 > /root/git/linuxkernel/diff/467-468.txt
echo writing to: /root/git/linuxkernel/diff/467-468.txt
git log --pretty=fuller -p --stat v2.6.28.8 v2.6.28.9 > /root/git/linuxkernel/diff/468-469.txt
echo writing to: /root/git/linuxkernel/diff/468-469.txt
git log --pretty=fuller -p --stat v2.6.28.9 v2.6.28.10 > /root/git/linuxkernel/diff/469-470.txt
echo writing to: /root/git/linuxkernel/diff/469-470.txt
git log --pretty=fuller -p --stat v2.6.28.10 v2.6.29 > /root/git/linuxkernel/diff/470-471.txt
echo writing to: /root/git/linuxkernel/diff/470-471.txt
git log --pretty=fuller -p --stat v2.6.29 v2.6.29-rc1 > /root/git/linuxkernel/diff/471-472.txt
echo writing to: /root/git/linuxkernel/diff/471-472.txt
git log --pretty=fuller -p --stat v2.6.29-rc1 v2.6.29-rc2 > /root/git/linuxkernel/diff/472-473.txt
echo writing to: /root/git/linuxkernel/diff/472-473.txt
git log --pretty=fuller -p --stat v2.6.29-rc2 v2.6.29-rc3 > /root/git/linuxkernel/diff/473-474.txt
echo writing to: /root/git/linuxkernel/diff/473-474.txt
git log --pretty=fuller -p --stat v2.6.29-rc3 v2.6.29-rc4 > /root/git/linuxkernel/diff/474-475.txt
echo writing to: /root/git/linuxkernel/diff/474-475.txt
git log --pretty=fuller -p --stat v2.6.29-rc4 v2.6.29-rc5 > /root/git/linuxkernel/diff/475-476.txt
echo writing to: /root/git/linuxkernel/diff/475-476.txt
git log --pretty=fuller -p --stat v2.6.29-rc5 v2.6.29-rc6 > /root/git/linuxkernel/diff/476-477.txt
echo writing to: /root/git/linuxkernel/diff/476-477.txt
git log --pretty=fuller -p --stat v2.6.29-rc6 v2.6.29-rc7 > /root/git/linuxkernel/diff/477-478.txt
echo writing to: /root/git/linuxkernel/diff/477-478.txt
git log --pretty=fuller -p --stat v2.6.29-rc7 v2.6.29-rc8 > /root/git/linuxkernel/diff/478-479.txt
echo writing to: /root/git/linuxkernel/diff/478-479.txt
git log --pretty=fuller -p --stat v2.6.29-rc8 v2.6.29.1 > /root/git/linuxkernel/diff/479-480.txt
echo writing to: /root/git/linuxkernel/diff/479-480.txt
git log --pretty=fuller -p --stat v2.6.29.1 v2.6.29.2 > /root/git/linuxkernel/diff/480-481.txt
echo writing to: /root/git/linuxkernel/diff/480-481.txt
git log --pretty=fuller -p --stat v2.6.29.2 v2.6.29.3 > /root/git/linuxkernel/diff/481-482.txt
echo writing to: /root/git/linuxkernel/diff/481-482.txt
git log --pretty=fuller -p --stat v2.6.29.3 v2.6.29.4 > /root/git/linuxkernel/diff/482-483.txt
echo writing to: /root/git/linuxkernel/diff/482-483.txt
git log --pretty=fuller -p --stat v2.6.29.4 v2.6.29.5 > /root/git/linuxkernel/diff/483-484.txt
echo writing to: /root/git/linuxkernel/diff/483-484.txt
git log --pretty=fuller -p --stat v2.6.29.5 v2.6.29.6 > /root/git/linuxkernel/diff/484-485.txt
echo writing to: /root/git/linuxkernel/diff/484-485.txt
git log --pretty=fuller -p --stat v2.6.29.6 v2.6.30 > /root/git/linuxkernel/diff/485-486.txt
echo writing to: /root/git/linuxkernel/diff/485-486.txt
git log --pretty=fuller -p --stat v2.6.30 v2.6.30-rc1 > /root/git/linuxkernel/diff/486-487.txt
echo writing to: /root/git/linuxkernel/diff/486-487.txt
git log --pretty=fuller -p --stat v2.6.30-rc1 v2.6.30-rc2 > /root/git/linuxkernel/diff/487-488.txt
echo writing to: /root/git/linuxkernel/diff/487-488.txt
git log --pretty=fuller -p --stat v2.6.30-rc2 v2.6.30-rc3 > /root/git/linuxkernel/diff/488-489.txt
echo writing to: /root/git/linuxkernel/diff/488-489.txt
git log --pretty=fuller -p --stat v2.6.30-rc3 v2.6.30-rc4 > /root/git/linuxkernel/diff/489-490.txt
echo writing to: /root/git/linuxkernel/diff/489-490.txt
git log --pretty=fuller -p --stat v2.6.30-rc4 v2.6.30-rc5 > /root/git/linuxkernel/diff/490-491.txt
echo writing to: /root/git/linuxkernel/diff/490-491.txt
git log --pretty=fuller -p --stat v2.6.30-rc5 v2.6.30-rc6 > /root/git/linuxkernel/diff/491-492.txt
echo writing to: /root/git/linuxkernel/diff/491-492.txt
git log --pretty=fuller -p --stat v2.6.30-rc6 v2.6.30-rc7 > /root/git/linuxkernel/diff/492-493.txt
echo writing to: /root/git/linuxkernel/diff/492-493.txt
git log --pretty=fuller -p --stat v2.6.30-rc7 v2.6.30-rc8 > /root/git/linuxkernel/diff/493-494.txt
echo writing to: /root/git/linuxkernel/diff/493-494.txt
git log --pretty=fuller -p --stat v2.6.30-rc8 v2.6.30.1 > /root/git/linuxkernel/diff/494-495.txt
echo writing to: /root/git/linuxkernel/diff/494-495.txt
git log --pretty=fuller -p --stat v2.6.30.1 v2.6.30.2 > /root/git/linuxkernel/diff/495-496.txt
echo writing to: /root/git/linuxkernel/diff/495-496.txt
git log --pretty=fuller -p --stat v2.6.30.2 v2.6.30.3 > /root/git/linuxkernel/diff/496-497.txt
echo writing to: /root/git/linuxkernel/diff/496-497.txt
git log --pretty=fuller -p --stat v2.6.30.3 v2.6.30.4 > /root/git/linuxkernel/diff/497-498.txt
echo writing to: /root/git/linuxkernel/diff/497-498.txt
git log --pretty=fuller -p --stat v2.6.30.4 v2.6.30.5 > /root/git/linuxkernel/diff/498-499.txt
echo writing to: /root/git/linuxkernel/diff/498-499.txt
git log --pretty=fuller -p --stat v2.6.30.5 v2.6.30.6 > /root/git/linuxkernel/diff/499-500.txt
echo writing to: /root/git/linuxkernel/diff/499-500.txt
git log --pretty=fuller -p --stat v2.6.30.6 v2.6.30.7 > /root/git/linuxkernel/diff/500-501.txt
echo writing to: /root/git/linuxkernel/diff/500-501.txt
git log --pretty=fuller -p --stat v2.6.30.7 v2.6.30.8 > /root/git/linuxkernel/diff/501-502.txt
echo writing to: /root/git/linuxkernel/diff/501-502.txt
git log --pretty=fuller -p --stat v2.6.30.8 v2.6.30.9 > /root/git/linuxkernel/diff/502-503.txt
echo writing to: /root/git/linuxkernel/diff/502-503.txt
git log --pretty=fuller -p --stat v2.6.30.9 v2.6.30.10 > /root/git/linuxkernel/diff/503-504.txt
echo writing to: /root/git/linuxkernel/diff/503-504.txt
git log --pretty=fuller -p --stat v2.6.30.10 v2.6.31 > /root/git/linuxkernel/diff/504-505.txt
echo writing to: /root/git/linuxkernel/diff/504-505.txt
git log --pretty=fuller -p --stat v2.6.31 v2.6.31-rc1 > /root/git/linuxkernel/diff/505-506.txt
echo writing to: /root/git/linuxkernel/diff/505-506.txt
git log --pretty=fuller -p --stat v2.6.31-rc1 v2.6.31-rc2 > /root/git/linuxkernel/diff/506-507.txt
echo writing to: /root/git/linuxkernel/diff/506-507.txt
git log --pretty=fuller -p --stat v2.6.31-rc2 v2.6.31-rc3 > /root/git/linuxkernel/diff/507-508.txt
echo writing to: /root/git/linuxkernel/diff/507-508.txt
git log --pretty=fuller -p --stat v2.6.31-rc3 v2.6.31-rc4 > /root/git/linuxkernel/diff/508-509.txt
echo writing to: /root/git/linuxkernel/diff/508-509.txt
git log --pretty=fuller -p --stat v2.6.31-rc4 v2.6.31-rc5 > /root/git/linuxkernel/diff/509-510.txt
echo writing to: /root/git/linuxkernel/diff/509-510.txt
git log --pretty=fuller -p --stat v2.6.31-rc5 v2.6.31-rc6 > /root/git/linuxkernel/diff/510-511.txt
echo writing to: /root/git/linuxkernel/diff/510-511.txt
git log --pretty=fuller -p --stat v2.6.31-rc6 v2.6.31-rc7 > /root/git/linuxkernel/diff/511-512.txt
echo writing to: /root/git/linuxkernel/diff/511-512.txt
git log --pretty=fuller -p --stat v2.6.31-rc7 v2.6.31-rc8 > /root/git/linuxkernel/diff/512-513.txt
echo writing to: /root/git/linuxkernel/diff/512-513.txt
git log --pretty=fuller -p --stat v2.6.31-rc8 v2.6.31-rc9 > /root/git/linuxkernel/diff/513-514.txt
echo writing to: /root/git/linuxkernel/diff/513-514.txt
git log --pretty=fuller -p --stat v2.6.31-rc9 v2.6.31.1 > /root/git/linuxkernel/diff/514-515.txt
echo writing to: /root/git/linuxkernel/diff/514-515.txt
git log --pretty=fuller -p --stat v2.6.31.1 v2.6.31.2 > /root/git/linuxkernel/diff/515-516.txt
echo writing to: /root/git/linuxkernel/diff/515-516.txt
git log --pretty=fuller -p --stat v2.6.31.2 v2.6.31.3 > /root/git/linuxkernel/diff/516-517.txt
echo writing to: /root/git/linuxkernel/diff/516-517.txt
git log --pretty=fuller -p --stat v2.6.31.3 v2.6.31.4 > /root/git/linuxkernel/diff/517-518.txt
echo writing to: /root/git/linuxkernel/diff/517-518.txt
git log --pretty=fuller -p --stat v2.6.31.4 v2.6.31.5 > /root/git/linuxkernel/diff/518-519.txt
echo writing to: /root/git/linuxkernel/diff/518-519.txt
git log --pretty=fuller -p --stat v2.6.31.5 v2.6.31.6 > /root/git/linuxkernel/diff/519-520.txt
echo writing to: /root/git/linuxkernel/diff/519-520.txt
git log --pretty=fuller -p --stat v2.6.31.6 v2.6.31.7 > /root/git/linuxkernel/diff/520-521.txt
echo writing to: /root/git/linuxkernel/diff/520-521.txt
git log --pretty=fuller -p --stat v2.6.31.7 v2.6.31.8 > /root/git/linuxkernel/diff/521-522.txt
echo writing to: /root/git/linuxkernel/diff/521-522.txt
git log --pretty=fuller -p --stat v2.6.31.8 v2.6.31.9 > /root/git/linuxkernel/diff/522-523.txt
echo writing to: /root/git/linuxkernel/diff/522-523.txt
git log --pretty=fuller -p --stat v2.6.31.9 v2.6.31.10 > /root/git/linuxkernel/diff/523-524.txt
echo writing to: /root/git/linuxkernel/diff/523-524.txt
git log --pretty=fuller -p --stat v2.6.31.10 v2.6.31.11 > /root/git/linuxkernel/diff/524-525.txt
echo writing to: /root/git/linuxkernel/diff/524-525.txt
git log --pretty=fuller -p --stat v2.6.31.11 v2.6.31.12 > /root/git/linuxkernel/diff/525-526.txt
echo writing to: /root/git/linuxkernel/diff/525-526.txt
git log --pretty=fuller -p --stat v2.6.31.12 v2.6.31.13 > /root/git/linuxkernel/diff/526-527.txt
echo writing to: /root/git/linuxkernel/diff/526-527.txt
git log --pretty=fuller -p --stat v2.6.31.13 v2.6.31.14 > /root/git/linuxkernel/diff/527-528.txt
echo writing to: /root/git/linuxkernel/diff/527-528.txt
git log --pretty=fuller -p --stat v2.6.31.14 v2.6.32 > /root/git/linuxkernel/diff/528-529.txt
echo writing to: /root/git/linuxkernel/diff/528-529.txt
git log --pretty=fuller -p --stat v2.6.32 v2.6.32-rc1 > /root/git/linuxkernel/diff/529-530.txt
echo writing to: /root/git/linuxkernel/diff/529-530.txt
git log --pretty=fuller -p --stat v2.6.32-rc1 v2.6.32-rc2 > /root/git/linuxkernel/diff/530-531.txt
echo writing to: /root/git/linuxkernel/diff/530-531.txt
git log --pretty=fuller -p --stat v2.6.32-rc2 v2.6.32-rc3 > /root/git/linuxkernel/diff/531-532.txt
echo writing to: /root/git/linuxkernel/diff/531-532.txt
git log --pretty=fuller -p --stat v2.6.32-rc3 v2.6.32-rc4 > /root/git/linuxkernel/diff/532-533.txt
echo writing to: /root/git/linuxkernel/diff/532-533.txt
git log --pretty=fuller -p --stat v2.6.32-rc4 v2.6.32-rc5 > /root/git/linuxkernel/diff/533-534.txt
echo writing to: /root/git/linuxkernel/diff/533-534.txt
git log --pretty=fuller -p --stat v2.6.32-rc5 v2.6.32-rc6 > /root/git/linuxkernel/diff/534-535.txt
echo writing to: /root/git/linuxkernel/diff/534-535.txt
git log --pretty=fuller -p --stat v2.6.32-rc6 v2.6.32-rc7 > /root/git/linuxkernel/diff/535-536.txt
echo writing to: /root/git/linuxkernel/diff/535-536.txt
git log --pretty=fuller -p --stat v2.6.32-rc7 v2.6.32-rc8 > /root/git/linuxkernel/diff/536-537.txt
echo writing to: /root/git/linuxkernel/diff/536-537.txt
git log --pretty=fuller -p --stat v2.6.32-rc8 v2.6.32.1 > /root/git/linuxkernel/diff/537-538.txt
echo writing to: /root/git/linuxkernel/diff/537-538.txt
git log --pretty=fuller -p --stat v2.6.32.1 v2.6.32.2 > /root/git/linuxkernel/diff/538-539.txt
echo writing to: /root/git/linuxkernel/diff/538-539.txt
git log --pretty=fuller -p --stat v2.6.32.2 v2.6.32.3 > /root/git/linuxkernel/diff/539-540.txt
echo writing to: /root/git/linuxkernel/diff/539-540.txt
git log --pretty=fuller -p --stat v2.6.32.3 v2.6.32.4 > /root/git/linuxkernel/diff/540-541.txt
echo writing to: /root/git/linuxkernel/diff/540-541.txt
git log --pretty=fuller -p --stat v2.6.32.4 v2.6.32.5 > /root/git/linuxkernel/diff/541-542.txt
echo writing to: /root/git/linuxkernel/diff/541-542.txt
git log --pretty=fuller -p --stat v2.6.32.5 v2.6.32.6 > /root/git/linuxkernel/diff/542-543.txt
echo writing to: /root/git/linuxkernel/diff/542-543.txt
git log --pretty=fuller -p --stat v2.6.32.6 v2.6.32.7 > /root/git/linuxkernel/diff/543-544.txt
echo writing to: /root/git/linuxkernel/diff/543-544.txt
git log --pretty=fuller -p --stat v2.6.32.7 v2.6.32.8 > /root/git/linuxkernel/diff/544-545.txt
echo writing to: /root/git/linuxkernel/diff/544-545.txt
git log --pretty=fuller -p --stat v2.6.32.8 v2.6.32.9 > /root/git/linuxkernel/diff/545-546.txt
echo writing to: /root/git/linuxkernel/diff/545-546.txt
git log --pretty=fuller -p --stat v2.6.32.9 v2.6.32.10 > /root/git/linuxkernel/diff/546-547.txt
echo writing to: /root/git/linuxkernel/diff/546-547.txt
git log --pretty=fuller -p --stat v2.6.32.10 v2.6.32.11 > /root/git/linuxkernel/diff/547-548.txt
echo writing to: /root/git/linuxkernel/diff/547-548.txt
git log --pretty=fuller -p --stat v2.6.32.11 v2.6.32.12 > /root/git/linuxkernel/diff/548-549.txt
echo writing to: /root/git/linuxkernel/diff/548-549.txt
git log --pretty=fuller -p --stat v2.6.32.12 v2.6.32.13 > /root/git/linuxkernel/diff/549-550.txt
echo writing to: /root/git/linuxkernel/diff/549-550.txt
git log --pretty=fuller -p --stat v2.6.32.13 v2.6.32.14 > /root/git/linuxkernel/diff/550-551.txt
echo writing to: /root/git/linuxkernel/diff/550-551.txt
git log --pretty=fuller -p --stat v2.6.32.14 v2.6.32.15 > /root/git/linuxkernel/diff/551-552.txt
echo writing to: /root/git/linuxkernel/diff/551-552.txt
git log --pretty=fuller -p --stat v2.6.32.15 v2.6.32.16 > /root/git/linuxkernel/diff/552-553.txt
echo writing to: /root/git/linuxkernel/diff/552-553.txt
git log --pretty=fuller -p --stat v2.6.32.16 v2.6.32.17 > /root/git/linuxkernel/diff/553-554.txt
echo writing to: /root/git/linuxkernel/diff/553-554.txt
git log --pretty=fuller -p --stat v2.6.32.17 v2.6.32.18 > /root/git/linuxkernel/diff/554-555.txt
echo writing to: /root/git/linuxkernel/diff/554-555.txt
git log --pretty=fuller -p --stat v2.6.32.18 v2.6.32.19 > /root/git/linuxkernel/diff/555-556.txt
echo writing to: /root/git/linuxkernel/diff/555-556.txt
git log --pretty=fuller -p --stat v2.6.32.19 v2.6.32.20 > /root/git/linuxkernel/diff/556-557.txt
echo writing to: /root/git/linuxkernel/diff/556-557.txt
git log --pretty=fuller -p --stat v2.6.32.20 v2.6.32.21 > /root/git/linuxkernel/diff/557-558.txt
echo writing to: /root/git/linuxkernel/diff/557-558.txt
git log --pretty=fuller -p --stat v2.6.32.21 v2.6.32.22 > /root/git/linuxkernel/diff/558-559.txt
echo writing to: /root/git/linuxkernel/diff/558-559.txt
git log --pretty=fuller -p --stat v2.6.32.22 v2.6.32.23 > /root/git/linuxkernel/diff/559-560.txt
echo writing to: /root/git/linuxkernel/diff/559-560.txt
git log --pretty=fuller -p --stat v2.6.32.23 v2.6.32.24 > /root/git/linuxkernel/diff/560-561.txt
echo writing to: /root/git/linuxkernel/diff/560-561.txt
git log --pretty=fuller -p --stat v2.6.32.24 v2.6.32.25 > /root/git/linuxkernel/diff/561-562.txt
echo writing to: /root/git/linuxkernel/diff/561-562.txt
git log --pretty=fuller -p --stat v2.6.32.25 v2.6.32.26 > /root/git/linuxkernel/diff/562-563.txt
echo writing to: /root/git/linuxkernel/diff/562-563.txt
git log --pretty=fuller -p --stat v2.6.32.26 v2.6.32.27 > /root/git/linuxkernel/diff/563-564.txt
echo writing to: /root/git/linuxkernel/diff/563-564.txt
git log --pretty=fuller -p --stat v2.6.32.27 v2.6.32.28 > /root/git/linuxkernel/diff/564-565.txt
echo writing to: /root/git/linuxkernel/diff/564-565.txt
git log --pretty=fuller -p --stat v2.6.32.28 v2.6.32.29 > /root/git/linuxkernel/diff/565-566.txt
echo writing to: /root/git/linuxkernel/diff/565-566.txt
git log --pretty=fuller -p --stat v2.6.32.29 v2.6.32.30 > /root/git/linuxkernel/diff/566-567.txt
echo writing to: /root/git/linuxkernel/diff/566-567.txt
git log --pretty=fuller -p --stat v2.6.32.30 v2.6.32.31 > /root/git/linuxkernel/diff/567-568.txt
echo writing to: /root/git/linuxkernel/diff/567-568.txt
git log --pretty=fuller -p --stat v2.6.32.31 v2.6.32.32 > /root/git/linuxkernel/diff/568-569.txt
echo writing to: /root/git/linuxkernel/diff/568-569.txt
git log --pretty=fuller -p --stat v2.6.32.32 v2.6.32.33 > /root/git/linuxkernel/diff/569-570.txt
echo writing to: /root/git/linuxkernel/diff/569-570.txt
git log --pretty=fuller -p --stat v2.6.32.33 v2.6.32.34 > /root/git/linuxkernel/diff/570-571.txt
echo writing to: /root/git/linuxkernel/diff/570-571.txt
git log --pretty=fuller -p --stat v2.6.32.34 v2.6.32.35 > /root/git/linuxkernel/diff/571-572.txt
echo writing to: /root/git/linuxkernel/diff/571-572.txt
git log --pretty=fuller -p --stat v2.6.32.35 v2.6.32.36 > /root/git/linuxkernel/diff/572-573.txt
echo writing to: /root/git/linuxkernel/diff/572-573.txt
git log --pretty=fuller -p --stat v2.6.32.36 v2.6.32.37 > /root/git/linuxkernel/diff/573-574.txt
echo writing to: /root/git/linuxkernel/diff/573-574.txt
git log --pretty=fuller -p --stat v2.6.32.37 v2.6.32.38 > /root/git/linuxkernel/diff/574-575.txt
echo writing to: /root/git/linuxkernel/diff/574-575.txt
git log --pretty=fuller -p --stat v2.6.32.38 v2.6.32.39 > /root/git/linuxkernel/diff/575-576.txt
echo writing to: /root/git/linuxkernel/diff/575-576.txt
git log --pretty=fuller -p --stat v2.6.32.39 v2.6.32.40 > /root/git/linuxkernel/diff/576-577.txt
echo writing to: /root/git/linuxkernel/diff/576-577.txt
git log --pretty=fuller -p --stat v2.6.32.40 v2.6.32.41 > /root/git/linuxkernel/diff/577-578.txt
echo writing to: /root/git/linuxkernel/diff/577-578.txt
git log --pretty=fuller -p --stat v2.6.32.41 v2.6.32.42 > /root/git/linuxkernel/diff/578-579.txt
echo writing to: /root/git/linuxkernel/diff/578-579.txt
git log --pretty=fuller -p --stat v2.6.32.42 v2.6.32.43 > /root/git/linuxkernel/diff/579-580.txt
echo writing to: /root/git/linuxkernel/diff/579-580.txt
git log --pretty=fuller -p --stat v2.6.32.43 v2.6.32.44 > /root/git/linuxkernel/diff/580-581.txt
echo writing to: /root/git/linuxkernel/diff/580-581.txt
git log --pretty=fuller -p --stat v2.6.32.44 v2.6.32.45 > /root/git/linuxkernel/diff/581-582.txt
echo writing to: /root/git/linuxkernel/diff/581-582.txt
git log --pretty=fuller -p --stat v2.6.32.45 v2.6.32.46 > /root/git/linuxkernel/diff/582-583.txt
echo writing to: /root/git/linuxkernel/diff/582-583.txt
git log --pretty=fuller -p --stat v2.6.32.46 v2.6.32.47 > /root/git/linuxkernel/diff/583-584.txt
echo writing to: /root/git/linuxkernel/diff/583-584.txt
git log --pretty=fuller -p --stat v2.6.32.47 v2.6.32.48 > /root/git/linuxkernel/diff/584-585.txt
echo writing to: /root/git/linuxkernel/diff/584-585.txt
git log --pretty=fuller -p --stat v2.6.32.48 v2.6.32.49 > /root/git/linuxkernel/diff/585-586.txt
echo writing to: /root/git/linuxkernel/diff/585-586.txt
git log --pretty=fuller -p --stat v2.6.32.49 v2.6.32.50 > /root/git/linuxkernel/diff/586-587.txt
echo writing to: /root/git/linuxkernel/diff/586-587.txt
git log --pretty=fuller -p --stat v2.6.32.50 v2.6.32.51 > /root/git/linuxkernel/diff/587-588.txt
echo writing to: /root/git/linuxkernel/diff/587-588.txt
git log --pretty=fuller -p --stat v2.6.32.51 v2.6.32.52 > /root/git/linuxkernel/diff/588-589.txt
echo writing to: /root/git/linuxkernel/diff/588-589.txt
git log --pretty=fuller -p --stat v2.6.32.52 v2.6.32.53 > /root/git/linuxkernel/diff/589-590.txt
echo writing to: /root/git/linuxkernel/diff/589-590.txt
git log --pretty=fuller -p --stat v2.6.32.53 v2.6.32.54 > /root/git/linuxkernel/diff/590-591.txt
echo writing to: /root/git/linuxkernel/diff/590-591.txt
git log --pretty=fuller -p --stat v2.6.32.54 v2.6.32.55 > /root/git/linuxkernel/diff/591-592.txt
echo writing to: /root/git/linuxkernel/diff/591-592.txt
git log --pretty=fuller -p --stat v2.6.32.55 v2.6.32.56 > /root/git/linuxkernel/diff/592-593.txt
echo writing to: /root/git/linuxkernel/diff/592-593.txt
git log --pretty=fuller -p --stat v2.6.32.56 v2.6.32.57 > /root/git/linuxkernel/diff/593-594.txt
echo writing to: /root/git/linuxkernel/diff/593-594.txt
git log --pretty=fuller -p --stat v2.6.32.57 v2.6.32.58 > /root/git/linuxkernel/diff/594-595.txt
echo writing to: /root/git/linuxkernel/diff/594-595.txt
git log --pretty=fuller -p --stat v2.6.32.58 v2.6.32.59 > /root/git/linuxkernel/diff/595-596.txt
echo writing to: /root/git/linuxkernel/diff/595-596.txt
git log --pretty=fuller -p --stat v2.6.32.59 v2.6.32.60 > /root/git/linuxkernel/diff/596-597.txt
echo writing to: /root/git/linuxkernel/diff/596-597.txt
git log --pretty=fuller -p --stat v2.6.32.60 v2.6.32.61 > /root/git/linuxkernel/diff/597-598.txt
echo writing to: /root/git/linuxkernel/diff/597-598.txt
git log --pretty=fuller -p --stat v2.6.32.61 v2.6.33 > /root/git/linuxkernel/diff/598-599.txt
echo writing to: /root/git/linuxkernel/diff/598-599.txt
git log --pretty=fuller -p --stat v2.6.33 v2.6.33-rc1 > /root/git/linuxkernel/diff/599-600.txt
echo writing to: /root/git/linuxkernel/diff/599-600.txt
git log --pretty=fuller -p --stat v2.6.33-rc1 v2.6.33-rc2 > /root/git/linuxkernel/diff/600-601.txt
echo writing to: /root/git/linuxkernel/diff/600-601.txt
git log --pretty=fuller -p --stat v2.6.33-rc2 v2.6.33-rc3 > /root/git/linuxkernel/diff/601-602.txt
echo writing to: /root/git/linuxkernel/diff/601-602.txt
git log --pretty=fuller -p --stat v2.6.33-rc3 v2.6.33-rc4 > /root/git/linuxkernel/diff/602-603.txt
echo writing to: /root/git/linuxkernel/diff/602-603.txt
git log --pretty=fuller -p --stat v2.6.33-rc4 v2.6.33-rc5 > /root/git/linuxkernel/diff/603-604.txt
echo writing to: /root/git/linuxkernel/diff/603-604.txt
git log --pretty=fuller -p --stat v2.6.33-rc5 v2.6.33-rc6 > /root/git/linuxkernel/diff/604-605.txt
echo writing to: /root/git/linuxkernel/diff/604-605.txt
git log --pretty=fuller -p --stat v2.6.33-rc6 v2.6.33-rc7 > /root/git/linuxkernel/diff/605-606.txt
echo writing to: /root/git/linuxkernel/diff/605-606.txt
git log --pretty=fuller -p --stat v2.6.33-rc7 v2.6.33-rc8 > /root/git/linuxkernel/diff/606-607.txt
echo writing to: /root/git/linuxkernel/diff/606-607.txt
git log --pretty=fuller -p --stat v2.6.33-rc8 v2.6.33.1 > /root/git/linuxkernel/diff/607-608.txt
echo writing to: /root/git/linuxkernel/diff/607-608.txt
git log --pretty=fuller -p --stat v2.6.33.1 v2.6.33.2 > /root/git/linuxkernel/diff/608-609.txt
echo writing to: /root/git/linuxkernel/diff/608-609.txt
git log --pretty=fuller -p --stat v2.6.33.2 v2.6.33.3 > /root/git/linuxkernel/diff/609-610.txt
echo writing to: /root/git/linuxkernel/diff/609-610.txt
git log --pretty=fuller -p --stat v2.6.33.3 v2.6.33.4 > /root/git/linuxkernel/diff/610-611.txt
echo writing to: /root/git/linuxkernel/diff/610-611.txt
git log --pretty=fuller -p --stat v2.6.33.4 v2.6.33.5 > /root/git/linuxkernel/diff/611-612.txt
echo writing to: /root/git/linuxkernel/diff/611-612.txt
pause