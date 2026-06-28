export function formatMemberLabel(member) {
  if (!member) return 'Unknown';

  if (member.name && member.email) {
    return `${member.name} (${member.email})`;
  }

  return member.name || member.email || `Member #${member.id}`;
}

export function getMemberDisplayName(memberOrId, members = []) {
  if (typeof memberOrId === 'object' && memberOrId !== null) {
    return formatMemberLabel(memberOrId);
  }

  const member = members.find((m) => m.id === memberOrId);
  if (member) return formatMemberLabel(member);

  return memberOrId ? `Member #${memberOrId}` : 'Unassigned';
}
