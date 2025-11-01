# TODO List for Fixing Dashboard and Profile Issues

1. **Fix welcome text color in DashboardScreen**: Change the color of "Chào mừng đến Dashboard!" to ensure better contrast against the light gray background (0xFFF5F5F5). Use a darker color like Color.Black or Color(0xFF333333) for visibility. ✅ Done

2. **Make date of birth editable in ProfileScreen**: Replace the static text for "Date of Birth" with an editable TextField. Add state management using remember { mutableStateOf } to store and update the date value. ✅ Done
